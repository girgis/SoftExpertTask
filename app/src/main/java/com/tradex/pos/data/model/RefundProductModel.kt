package com.tradex.pos.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class RefundProductModel (
    var session_id: String = "",
    var data: List<RefundProduct>
)