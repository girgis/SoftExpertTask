package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductModel (
    var message: String = "",
    var data: List<Product>,
    var error: List<String>,
    var success: Boolean
)