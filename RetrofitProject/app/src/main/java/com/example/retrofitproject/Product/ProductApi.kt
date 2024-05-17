package com.example.retrofitproject.Product

import com.example.retrofitproject.DataClasses.Comment
import com.example.retrofitproject.DataClasses.SocialMapObject
import com.example.retrofitproject.DataClasses.User
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductApi {
    @GET("api/SocialMapObject")
    suspend fun getSocialMapObject(): List<SocialMapObject>

    @GET("api/comment/{mapObjectId}")
    suspend fun getComments(@Path("mapObjectId") mapObjectId: Int): List<Comment>


    @POST("api/comment/")
    suspend fun addComment(@Query("text") text: String, @Query("email") email: String, @Query("mapObjectId") mapObjectId: Int)

    @GET("api/users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @POST("api/users/")
    suspend fun addUser(@Query("name") name: String,
                        @Query("type") type: Int,
                        @Query("email") email: String,
                        @Query("password") password: String)
}