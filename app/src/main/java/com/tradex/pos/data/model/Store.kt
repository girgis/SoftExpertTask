package com.tradex.pos.data.model

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Store(@SerializedName("id") val id : Int,
                 @SerializedName("enterprise_id") val enterprise_id : Int,
                 @SerializedName("brand_id") val brand_id : Int,
                 @SerializedName("parent_id") val parent_id : Int,
                 @SerializedName("name") val name : String,
                 @SerializedName("name_ar") val name_ar : String,
                 @SerializedName("description") val description : String,
                 @SerializedName("description_ar") val description_ar : String,
                 @SerializedName("meta_tag_title") val meta_tag_title : String,
                 @SerializedName("meta_tag_title_ar") val meta_tag_title_ar : String,
                 @SerializedName("meta_tag_description") val meta_tag_description : String,
                 @SerializedName("meta_tag_description_ar") val meta_tag_description_ar : String,
                 @SerializedName("meta_tag_keywords") val meta_tag_meta_tag_keywordswords : String,
                 @SerializedName("meta_tag_keywords_ar") val meta_tag_meta_tag_keywords_arwords_ar : String,
                 @SerializedName("image") val image : String,
                 @SerializedName("top") val top : String,
                 @SerializedName("columns") val columns : String,
                 @SerializedName("sort_order") val sort_order : String,
                 @SerializedName("status") val status : String,
                 @SerializedName("keyword_en") val keyword_enword_en : String,
                 @SerializedName("keyword_ar") val keyword_arword_ar : String,
                 @SerializedName("layout_override_id") val layout_override_id : String,
                 @SerializedName("created_at") val created_at : String,
                 @SerializedName("updated_at") val updated_at : String,
                 @SerializedName("deleted_at") val deleted_at : String,
                 @SerializedName("products") val products : List<Product>)