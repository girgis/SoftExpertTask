package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(val id: Int,
                val name: String,
                val email: String,
                val email_verified_at: String,
                val phone: String,
                val picture: String,
                val user_level_id: String,
                var lang: String,
                val api_token: String,
                val created_at: String,
                val updated_at: String,
                val deleted_at: String,
                val branch: Branch,
                val user: MUser
)