package com.example.retrofitproject.Product

import com.example.retrofitproject.DataClasses.Comment
import com.example.retrofitproject.DataClasses.RegisterBody
import com.example.retrofitproject.DataClasses.SocialMapObject
import com.example.retrofitproject.DataClasses.User
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ProductApi {
    @GET("api/SocialMapObject")
    suspend fun getSocialMapObject(): List<SocialMapObject>

    @GET("api/comment/{mapObjectId}")
    suspend fun getComments(@Path("mapObjectId") mapObjectId: Int): List<Comment>

    @FormUrlEncoded
    @POST("api/comment")
    suspend fun addComment(@Field("text") text: String,
                           @Field("email") email: String,
                           @Field("mapObjectId") mapObjectId: Int)

    @GET("api/users/{email}")
    suspend fun getUserByEmail(@Path("email") email: String): User

    @FormUrlEncoded
    @POST("api/users/")
    suspend fun addUser(@Field("name") name: String,
                        @Field("type") type: Int,
                        @Field("email") email: String,
                        @Field("password") password: String)
}