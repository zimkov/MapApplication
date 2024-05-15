package com.example.retrofitproject.DataClasses

data class SocialMapObject(
    val rating: Int,
    val id: Int,
    val x: Double,
    val y: Double,
    val display_name: String,
    val adress: String,
    val images: String,
    val type: String,
    val availability: String
)
