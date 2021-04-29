package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CartModel (
    var message: String = "",
    var data: Cart,
    var error: List<String>,
    var success: Boolean
)