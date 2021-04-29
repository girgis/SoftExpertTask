package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductStoreModel (
    var message: String = "",
    var data: ProductsPaginate,
    var error: List<String>,
    var success: Boolean
)