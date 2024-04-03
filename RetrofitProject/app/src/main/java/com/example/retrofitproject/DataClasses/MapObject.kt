package com.example.retrofitproject.DataClasses

import org.osmdroid.util.GeoPoint

data class MapObject(
    val id: Int,
    val display_name: String,
    val geoPoint: GeoPoint,
    val rating: Float,
    val address: String = "",
    val type: String =  "",
    val availability: String = "Нет информации о доступности"
)
