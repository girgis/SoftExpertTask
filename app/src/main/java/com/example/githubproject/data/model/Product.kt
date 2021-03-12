package com.example.githubproject.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product(
    @PrimaryKey(autoGenerate = true) val uid: Int?,
    @ColumnInfo(name = "prouduct_name") val ProductName: String?,
    @ColumnInfo(name = "product_image") val ProductImage: String?)