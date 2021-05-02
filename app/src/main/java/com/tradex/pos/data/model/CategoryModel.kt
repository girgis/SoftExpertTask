package com.tradex.pos.data.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CategoryModel (
    var message: String = "",
    var data: CategoryPaginate,
    var error: List<String>,
    var success: Boolean
)