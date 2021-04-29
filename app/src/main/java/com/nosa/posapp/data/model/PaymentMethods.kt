package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentMethods(val payment_methods: List<PaymentMethod>,
                          val country_code: String,
                          val currency_code: String
)