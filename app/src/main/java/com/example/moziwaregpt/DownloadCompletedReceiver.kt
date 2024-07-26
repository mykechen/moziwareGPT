package com.example.moziwaregpt

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.widget.Toast
import java.io.File

class DownloadCompletedReceiver: BroadcastReceiver() {

//    override fun onReceive(context: Context?, intent: Intent?) {
//        if(intent?.action == "android.intent.action.DOWNLOAD_COMPLETE") {
//            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
//            if(id != -1L) {
//                Toast.makeText(context, "COMPLETE", Toast.LENGTH_LONG).show()
//
//            }
//        }
//    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == DownloadManager.ACTION_DOWNLOAD_COMPLETE) {
            val id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
            if (id != -1L) {
                println("Download with ID $id finished!")
                context?.let { ctx ->
                    val downloadManager = ctx.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val query = DownloadManager.Query().setFilterById(id)
                    val cursor: Cursor = downloadManager.query(query)
                    if (cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                        if (columnIndex != -1 && cursor.getInt(columnIndex) == DownloadManager.STATUS_SUCCESSFUL) {
                            val uriIndex = cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            if (uriIndex != -1) {
                                val uri = cursor.getString(uriIndex)
                                uri?.let {
                                    val file = File(Uri.parse(it).path!!)
                                    PdfUtils.openDownloadedPdf(ctx, file.name)
                                }
                            }
                        }
                    }
                    cursor.close()
                }
            }
        }
    }
}