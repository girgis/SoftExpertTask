package com.nosa.posapp.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.nosa.posapp.data.model.Product


data class Products (
	@SerializedName("id") val id : Int,
	@SerializedName("order_id") val order_id : Int,
	@SerializedName("product_id") val product_id : Int,
	@SerializedName("quantity") val quantity : Int,
	@SerializedName("price") val price : Double,
	@SerializedName("total_price") val total_price : Double,
	@SerializedName("created_at") val created_at : String?,
	@SerializedName("updated_at") val updated_at : String?,
	@SerializedName("deleted_at") val deleted_at : String?,
	@SerializedName("product") val product : Product?
) : Parcelable {
	constructor(parcel: Parcel) : this(
		parcel.readInt(),
		parcel.readInt(),
		parcel.readInt(),
		parcel.readInt(),
		parcel.readDouble(),
		parcel.readDouble(),
		parcel.readString(),
		parcel.readString(),
		parcel.readString(),
		parcel.readParcelable(Product::class.java.classLoader)
	) {
	}

	override fun writeToParcel(parcel: Parcel, flags: Int) {
		parcel.writeInt(id)
		parcel.writeInt(order_id)
		parcel.writeInt(product_id)
		parcel.writeInt(quantity)
		parcel.writeDouble(price)
		parcel.writeDouble(total_price)
		parcel.writeString(created_at)
		parcel.writeString(updated_at)
		parcel.writeString(deleted_at)
		parcel.writeParcelable(product, flags)
	}

	override fun describeContents(): Int {
		return 0
	}

	companion object CREATOR : Parcelable.Creator<Products> {
		override fun createFromParcel(parcel: Parcel): Products {
			return Products(parcel)
		}

		override fun newArray(size: Int): Array<Products?> {
			return arrayOfNulls(size)
		}
	}
}