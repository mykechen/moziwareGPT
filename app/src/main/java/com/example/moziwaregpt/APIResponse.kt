package com.example.moziwaregpt

data class APIResponse(
    val code: Int,
    val msg: String,
    val data: GPTA,
    val success: Boolean,
    val error: Boolean
)

data class Advice(
    val reference: String,
    val pdf: String,
    val pdf_file_content: String,
    val content: String
)

data class GPTA(
    val question: String,
    val advice: Advice,
    val used: Double
)

