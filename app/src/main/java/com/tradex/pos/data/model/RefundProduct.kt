package com.tradex.pos.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefundProduct(val order_product_id: Int,
                         val return_quantity: Int,
                         val note: String
)