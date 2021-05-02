package com.tradex.pos.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Product(
    @SerializedName("id") val id : Int,
    @SerializedName("order_product_id") var order_product_id : Int,
    @SerializedName("enterprise_id") val enterprise_id : Int,
    @SerializedName("brand_id") val brand_id : Int,
    @SerializedName("name") val name : String?,
    @SerializedName("name_ar") val name_ar : String?,
    @SerializedName("description") val description : String?,
    @SerializedName("description_ar") val description_ar : String?,
    @SerializedName("meta_tag_title") val meta_tag_title : String?,
    @SerializedName("meta_tag_title_ar") val meta_tag_title_ar : String?,
    @SerializedName("meta_tag_description") val meta_tag_description : String?,
    @SerializedName("meta_tag_description_ar") val meta_tag_description_ar : String?,
    @SerializedName("meta_tag_keywords") val meta_tag_meta_tag_keywordswords : String?,
    @SerializedName("meta_tag_keywords_ar") val meta_tag_meta_tag_keywords_arwords_ar : String?,
    @SerializedName("product_tags") val product_tags : String?,
    @SerializedName("product_tags_ar") val product_tags_ar : String?,
    @SerializedName("price") val price : Double,
    @SerializedName("tax_id") val tax_id : String?,
    @SerializedName("image") val image : String?,
    @SerializedName("barcode") val barcode : String?,
    @SerializedName("model") val model : String?,
    @SerializedName("length") val length : String?,
    @SerializedName("width") val width : String?,
    @SerializedName("height") val height : String?,
    @SerializedName("length_class_id") val length_class_id : Int,
    @SerializedName("weight") val weight : String?,
    @SerializedName("weight_class_id") val weight_class_id : Int,
    @SerializedName("product_status_id") val product_status_id : Int,
    @SerializedName("sort_order") val sort_order : String?,
    @SerializedName("min_quantity") val min_quantity : String?,
    @SerializedName("max_quantity") val max_quantity : String?,
    @SerializedName("price_under_min_quantity") val price_under_min_quantity : String?,
    @SerializedName("price_upper_max_quantity") val price_upper_max_quantity : String?,
    @SerializedName("keyword_en") val keyword_enword_en : String?,
    @SerializedName("keyword_ar") val keyword_arword_ar : String?,
    @SerializedName("layout_override_id") val layout_override_id : String?,
    @SerializedName("internal_number") val internal_number : String?,
    @SerializedName("type_category_id") val type_category_id : Int,
    @SerializedName("main_category_id") val main_category_id : Int,
    @SerializedName("cost_price") val cost_price : String?,
    @SerializedName("brand") val brand : String?,
    @SerializedName("brand_logo") val brand_logo : String?,
    @SerializedName("agent") val agent : String?,
    @SerializedName("last_resource") val last_resource : String?,
    @SerializedName("quantity") var quantity : Int,
    @SerializedName("created_at") val created_at : String?,
    @SerializedName("updated_at") val updated_at : String?,
    @SerializedName("deleted_at") val deleted_at : String?,
    @SerializedName("final_price") val final_price : Double,
    @SerializedName("images") val images : List<Images>?,
    @SerializedName("reminder_quantity") val reminder_quantity : Int,
    @SerializedName("special") val special : String?,
    @SerializedName("tax") val tax : String?,
    @SerializedName("remaining_quantity") val remaining_quantity : String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.createTypedArrayList(Images.CREATOR),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(order_product_id)
        parcel.writeInt(enterprise_id)
        parcel.writeInt(brand_id)
        parcel.writeString(name)
        parcel.writeString(name_ar)
        parcel.writeString(description)
        parcel.writeString(description_ar)
        parcel.writeString(meta_tag_title)
        parcel.writeString(meta_tag_title_ar)
        parcel.writeString(meta_tag_description)
        parcel.writeString(meta_tag_description_ar)
        parcel.writeString(meta_tag_meta_tag_keywordswords)
        parcel.writeString(meta_tag_meta_tag_keywords_arwords_ar)
        parcel.writeString(product_tags)
        parcel.writeString(product_tags_ar)
        parcel.writeDouble(price)
        parcel.writeString(tax_id)
        parcel.writeString(image)
        parcel.writeString(barcode)
        parcel.writeString(model)
        parcel.writeString(length)
        parcel.writeString(width)
        parcel.writeString(height)
        parcel.writeInt(length_class_id)
        parcel.writeString(weight)
        parcel.writeInt(weight_class_id)
        parcel.writeInt(product_status_id)
        parcel.writeString(sort_order)
        parcel.writeString(min_quantity)
        parcel.writeString(max_quantity)
        parcel.writeString(price_under_min_quantity)
        parcel.writeString(price_upper_max_quantity)
        parcel.writeString(keyword_enword_en)
        parcel.writeString(keyword_arword_ar)
        parcel.writeString(layout_override_id)
        parcel.writeString(internal_number)
        parcel.writeInt(type_category_id)
        parcel.writeInt(main_category_id)
        parcel.writeString(cost_price)
        parcel.writeString(brand)
        parcel.writeString(brand_logo)
        parcel.writeString(agent)
        parcel.writeString(last_resource)
        parcel.writeInt(quantity)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(deleted_at)
        parcel.writeDouble(final_price)
        parcel.writeTypedList(images)
        parcel.writeInt(reminder_quantity)
        parcel.writeString(special)
        parcel.writeString(tax)
        parcel.writeString(remaining_quantity)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        when (other) {
            is Product -> {
                return this.id == other.id
            }
            else -> return false
        }
    }
}