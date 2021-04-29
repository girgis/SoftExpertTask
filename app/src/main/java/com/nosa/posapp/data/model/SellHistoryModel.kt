package com.nosa.posapp.data.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SellHistoryModel (
    var message: String = "",
    var data: CartPaginate,
    var error: List<String>,
    var success: Boolean
)