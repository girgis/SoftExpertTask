package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherDay(
    val date:String,
    val day:Day
)
