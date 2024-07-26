package com.example.moziwaregpt

interface Downloader {
    fun downloadFile(url: String): Long
}