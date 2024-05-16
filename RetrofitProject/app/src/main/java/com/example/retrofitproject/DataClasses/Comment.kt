package com.example.retrofitproject.DataClasses

data class Comment(
    val id: Int,
    val text: String?,
    val rate: Int,
    val userId: Int,
    val user: User,
    val mapObjectId: Int,
    val mapObject: SocialMapObject?
)
