package com.example.githubproject.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RegionModel(
    val data: ArrayList<Region>
)
