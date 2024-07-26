package com.example.moziwaregpt

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.widget.Toast
import androidx.core.net.toUri

class AndroidDownloader (
    private val context: Context
): Downloader {

    private val downloadManager = context.getSystemService(DownloadManager::class.java)

    override fun downloadFile(url: String): Long {
        val fileName = "moziwareGPT_${System.currentTimeMillis()}.pdf"
        val request = DownloadManager.Request(url.toUri())
            .setMimeType("pdf")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setTitle(fileName)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)

        Toast.makeText(context, "Downloading PDF...", Toast.LENGTH_SHORT).show()

        return downloadManager.enqueue(request)
    }
}