package com.example.bookxpert.API

import com.example.bookxpert.models.ApiResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("objects")
    suspend fun getObjects(): Response<List<ApiResponseItem>>
}
