package com.example.retrofitproject.DataClasses

data class SocialMapObject(
    val rating: Int,
    val id: Int,
    val x: Int,
    val y: Int,
    val display_name: String,
    val adress: String,
    val images: String,
    val type: String,
    val availability: String
)
