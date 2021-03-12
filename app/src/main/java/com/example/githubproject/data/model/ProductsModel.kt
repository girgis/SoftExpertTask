package com.example.githubproject.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductsModel (
    var status: String = "",
    var data : ArrayList<Product>){

}