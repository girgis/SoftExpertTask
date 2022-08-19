package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class WeatherModel(
    val location: Location,
    val current: Current,
    val forecast: Forecastday
)
