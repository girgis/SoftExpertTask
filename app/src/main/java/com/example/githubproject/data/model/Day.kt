package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Day(
    val maxtemp_f: Double,
    val mintemp_f: Double,
    val condition: Condition
)
