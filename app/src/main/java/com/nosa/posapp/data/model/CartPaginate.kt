package com.nosa.posapp.data.model

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CartPaginate(var current_page: Int,
                        var data: List<Cart>,
                        var first_page_url: String,
                        var from: Int,
                        var last_page: Int,
                        var last_page_url: String,
                        var next_page_url: String,
                        var path: String,
                        var per_page: String,
                        var prev_page_url: String,
                        var to: Int,
                        var total: Int
)