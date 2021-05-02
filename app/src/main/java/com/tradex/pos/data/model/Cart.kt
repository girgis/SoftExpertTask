package com.tradex.pos.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Cart (
	@SerializedName("enterprise_id") val enterprise_id : Int,
	@SerializedName("brand_id") val brand_id : Int,
	@SerializedName("branch_id") val branch_id : Int,
	@SerializedName("is_order") val is_order : Int,
	@SerializedName("updated_at") val updated_at : String?,
	@SerializedName("created_at") val created_at : String?,
	@SerializedName("first_name") val first_name : String?,
	@SerializedName("last_name") val last_name : String?,
	@SerializedName("phone") val phone : String?,
	@SerializedName("address") val address : String?,
	@SerializedName("email") val email : String?,
	@SerializedName("id") val id : Int,
	@SerializedName("is_done") val is_done : Int,
	@SerializedName("for_stock") val for_stock : Int,
	@SerializedName("session_id") val session_id : String?,
	@SerializedName("total") val total : Double,
	@SerializedName("products") val products : List<Products>?
): Parcelable {
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
		parcel.readInt(),
		parcel.readInt(),
		parcel.readInt(),
		parcel.readString(),
		parcel.readDouble(),
		parcel.createTypedArrayList(Products.CREATOR)
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(enterprise_id)
		parcel.writeInt(brand_id)
		parcel.writeInt(branch_id)
		parcel.writeInt(is_order)
		parcel.writeString(updated_at)
		parcel.writeString(created_at)
		parcel.writeString(first_name)
		parcel.writeString(last_name)
		parcel.writeString(phone)
		parcel.writeString(address)
		parcel.writeString(email)
		parcel.writeInt(id)
		parcel.writeInt(is_done)
		parcel.writeInt(for_stock)
		parcel.writeString(session_id)
		parcel.writeDouble(total)
		parcel.writeTypedList(products)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Cart> {
		override fun createFromParcel(parcel: Parcel): Cart {
			return Cart(parcel)
		}

		override fun newArray(size: Int): Array<Cart?> {
			return arrayOfNulls(size)
		}
	}
}