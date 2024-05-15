package com.example.retrofitproject.DataClasses

import android.os.Parcelable
import java.io.Serializable

data class User(
    val id: Int,
    val name: String,
    val type: String,
    val email: String,
    val password: String
) : Serializable
