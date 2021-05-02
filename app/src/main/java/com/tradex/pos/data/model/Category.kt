package com.tradex.pos.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Category(
    @SerializedName("id") val id: Int,
    @SerializedName("enterprise_id") val enterprise_id: Int,
    @SerializedName("brand_id") val brand_id: Int,
    @SerializedName("parent_id") val parent_id: Int,
    @SerializedName("name") val name: String?,
    @SerializedName("name_ar") val name_ar: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("description_ar") val description_ar: String?,
    @SerializedName("meta_tag_title") val meta_tag_title: String?,
    @SerializedName("meta_tag_title_ar") val meta_tag_title_ar: String?,
    @SerializedName("meta_tag_description") val meta_tag_description: String?,
    @SerializedName("meta_tag_description_ar") val meta_tag_description_ar: String?,
    @SerializedName("meta_tag_keywords") val meta_tag_meta_tag_keywordswords: String?,
    @SerializedName("meta_tag_keywords_ar") val meta_tag_meta_tag_keywords_arwords_ar: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("imagefullpath") val imagefullpath: String?,
    @SerializedName("top") val top: String?,
    @SerializedName("columns") val columns: String?,
    @SerializedName("sort_order") val sort_order: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("keyword_en") val keyword_enword_en: String?,
    @SerializedName("keyword_ar") val keyword_arword_ar: String?,
    @SerializedName("layout_override_id") val layout_override_id: String?,
    @SerializedName("created_at") val created_at: String?,
    @SerializedName("updated_at") val updated_at: String?,
    @SerializedName("deleted_at") val deleted_at: String?
) : Parcelable{
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
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(enterprise_id)
        parcel.writeInt(brand_id)
        parcel.writeInt(parent_id)
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
        parcel.writeString(image)
        parcel.writeString(imagefullpath)
        parcel.writeString(top)
        parcel.writeString(columns)
        parcel.writeString(sort_order)
        parcel.writeString(status)
        parcel.writeString(keyword_enword_en)
        parcel.writeString(keyword_arword_ar)
        parcel.writeString(layout_override_id)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(deleted_at)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }

}