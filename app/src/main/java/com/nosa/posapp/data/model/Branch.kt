package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Branch(val id: Int,
                  val enterprise_id: String,
                  val brand_id: String,
                  val name: String,
                  val name_ar: String,
                  val email: String,
                  val phone: String,
                  val user_id: String,
                  val logo: String,
                  val address: String,
                  val longitude: String,
                  val latitude: String,
                  val created_at: String,
                  val updated_at: String,
                  val deleted_at: String,
                  val logofullpath: String
)