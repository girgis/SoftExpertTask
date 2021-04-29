package com.nosa.posapp.data.model


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InvoiceViewModel (
    var message: String = "",
    var data: String,
    var error: List<String>,
    var success: Boolean
)