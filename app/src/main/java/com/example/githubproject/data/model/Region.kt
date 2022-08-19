package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Region(
    val id: Int,
    val name: String,
    val region: String,
    val country: String
)

