package com.example.retrofitproject.DataClasses

data class Comment(
    val Id: Int,
    val Text: String,
    val Rate: Int,
    val User: User,
    val MapObject: SocialMapObject
)
