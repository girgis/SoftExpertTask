package com.tradex.pos.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Images(@SerializedName("id") val id : Int,
                  @SerializedName("product_id") val product_id : Int,
                  @SerializedName("path") val path : String?,
                  @SerializedName("created_at") val created_at : String?,
                  @SerializedName("updated_at") val updated_at : String?,
                  @SerializedName("deleted_at") val deleted_at : String?,
                  @SerializedName("imagefullpath") val imagefullpath : String?): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(product_id)
        parcel.writeString(path)
        parcel.writeString(created_at)
        parcel.writeString(updated_at)
        parcel.writeString(deleted_at)
        parcel.writeString(imagefullpath)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Images> {
        override fun createFromParcel(parcel: Parcel): Images {
            return Images(parcel)
        }

        override fun newArray(size: Int): Array<Images?> {
            return arrayOfNulls(size)
        }
    }
}