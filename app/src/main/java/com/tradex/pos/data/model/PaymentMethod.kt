package com.tradex.pos.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentMethod(val id: Int,
                         val name: String,
                         val name_ar: String,
                         val icon: String,
                         val created_at: String,
                         val updated_at: String,
                         val deleted_at: String,
                         val imagefullpath: String
)