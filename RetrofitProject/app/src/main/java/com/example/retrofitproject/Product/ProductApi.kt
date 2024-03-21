package com.example.retrofitproject.Product

import retrofit2.http.GET

interface ProductApi {
    @GET("api/users")
    suspend fun getUser(): String
}