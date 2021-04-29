package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentMethodsModel (
    var message: String = "",
    var data: PaymentMethods,
    var error: List<String>,
    var success: Boolean
)