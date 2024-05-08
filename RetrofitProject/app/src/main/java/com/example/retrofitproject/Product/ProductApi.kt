package com.example.retrofitproject.Product

import com.example.retrofitproject.DataClasses.SocialMapObject
import retrofit2.http.GET

interface ProductApi {
    @GET("api/SocialMapObject")
    suspend fun getSocialMapObject(): List<SocialMapObject>
}