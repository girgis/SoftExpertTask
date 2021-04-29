package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ActivationCodeModel (
    var message: String = "",
    var data: Any,
    var error: List<String>,
    var success: Boolean
)