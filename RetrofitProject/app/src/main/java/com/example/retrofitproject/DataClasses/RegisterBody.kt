package com.example.retrofitproject.DataClasses

data class RegisterBody(
    val name: String,
    val type: Int,
    val email: String,
    val password: String
)
