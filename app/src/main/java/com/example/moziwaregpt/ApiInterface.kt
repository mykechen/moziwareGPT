package com.example.moziwaregpt

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("{question}")
    suspend fun getAnswer(@Path("question") question: String): Response<APIResponse>
}