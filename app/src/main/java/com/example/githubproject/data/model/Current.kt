package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Current(
    val temp_c: Double,
    val temp_f: Double,
    val condition: Condition,
    val wind_mph: Double,
    val humidity: Int
)
