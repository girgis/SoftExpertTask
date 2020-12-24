package com.example.githubproject.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(

    val id: Int,

    val brand: String,

    val constractionYear: String,

    val isUsed: Boolean,

    val imageUrl: String
)