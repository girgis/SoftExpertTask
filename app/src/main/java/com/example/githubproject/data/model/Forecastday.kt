package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Forecastday(
    val forecastday: ArrayList<WeatherDay>
)
