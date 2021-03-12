package com.example.githubproject.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.githubproject.data.db.DataConverter
import com.google.gson.annotations.SerializedName
import java.util.*

@Entity(tableName = "favoriteProducts")
data class FavoriteProducts(
    @PrimaryKey val product_id: Int,

    @ColumnInfo(name = "updated_at", defaultValue = "CURRENT_TIMESTAMP")
    @TypeConverters(DataConverter::class)
    val updatedAt: Date,

    @ColumnInfo(name = "product_name") val ProductName: String?,
    @ColumnInfo(name = "product_image") val ProductImage: String?){

    override fun equals(other: Any?): Boolean {
        if (other is FavoriteProducts){
            if ((other as FavoriteProducts).product_id == this.product_id)
                return true
        }
        return false
    }

    override fun hashCode(): Int {
        return product_id
    }

}