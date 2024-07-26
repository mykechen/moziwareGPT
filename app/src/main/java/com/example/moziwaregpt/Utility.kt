package com.example.moziwaregpt

import android.app.DownloadManager
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

object PdfUtils {
    private var downloadId: Long = -1

    fun downloadPdf(context: Context, url: String, fileName: String) {
        try {
            val request = DownloadManager.Request(Uri.parse(url))
                .setTitle(fileName)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadId = downloadManager.enqueue(request)

            Toast.makeText(context, "Downloading PDF...", Toast.LENGTH_SHORT).show()

            // Register BroadcastReceiver to listen for download completion
            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                    if (id == downloadId) {
                        context.unregisterReceiver(this)
                        openDownloadedPdf(context, fileName)
                    }
                }
            }
            context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        } catch (e: Exception) {
            Toast.makeText(context, "Download failed: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    fun openDownloadedPdf(context: Context, fileName: String) {
        Handler(Looper.getMainLooper()).post {
            try {
                val file = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
                if (file.exists()) {
                    val uri = FileProvider.getUriForFile(
                        context,
                        "${context.packageName}.fileprovider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(uri, "application/pdf")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)  // Add this line
                    }
                    context.startActivity(intent)
                } else {
                    Toast.makeText(context, "Downloaded file not found", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("PdfUtils", "Error opening PDF", e)
                Toast.makeText(context, "Error opening PDF: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun openPdfFile(context: Context, filePath: String) {
        val file = File(filePath)
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "No PDF viewer app found", Toast.LENGTH_SHORT).show()
        }
    }

    fun decodeBase64(context: Context, input: String, fileName: String) {
        GlobalFunUtils.showLoadingDialog("Parsing PDF...")
        val out = Base64.decode(input, Base64.DEFAULT)
        val path = getCachePdfPath(context, fileName)
        Log.d("PdfUtils", "Decoded PDF path: $path")
        var randomAccessFile: RandomAccessFile? = null
        try {
            randomAccessFile = RandomAccessFile(path, "rw")
            randomAccessFile.write(out)
            Log.d("PdfUtils", "PDF file written successfully")
            openPdfFile(context, path)
        } catch (e: IOException) {
            Log.e("PdfUtils", "Error writing PDF file", e)
            Toast.makeText(context, "Error writing PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e("PdfUtils", "Unexpected error", e)
            Toast.makeText(context, "Unexpected error: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            GlobalFunUtils.dismissLoadingDialog()
            randomAccessFile?.close()
        }
    }

    private fun getCachePdfPath(context: Context, fileName: String): String {
        val cacheDir = context.cacheDir
        val file = File(cacheDir, fileName)
        return file.absolutePath
    }
}

object Utils {
    fun isStringValid(str: String?): Boolean {
        return !str.isNullOrEmpty()
    }
}

object GlobalFunUtils {
    private var showDialog by mutableStateOf(false)
    private var dialogMessage by mutableStateOf("")

    fun showLoadingDialog(message: String) {
        dialogMessage = message
        showDialog = true
    }

    fun dismissLoadingDialog() {
        showDialog = false
    }

    fun showErrorPageThenLogin(context: Context, filePath: String, errorCode: Int) {
        // Implement error handling logic here
        Toast.makeText(context, "Error $errorCode occurred while processing $filePath", Toast.LENGTH_LONG).show()
        // You might want to start a login activity or show an error dialog here
    }

    @Composable
    fun LoadingDialog() {
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { },
                confirmButton = { },
                dismissButton = { },
                title = { Text(text = "Loading") },
                text = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = dialogMessage,
                            modifier = Modifier.padding(start = 32.dp, top = 8.dp)
                        )
                    }
                },
                properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
            )
        }
    }
}