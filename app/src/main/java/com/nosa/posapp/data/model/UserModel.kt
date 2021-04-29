package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserModel (
    var message: String = "",
    var data: User,
    var error: List<String>,
    var success: Boolean
)