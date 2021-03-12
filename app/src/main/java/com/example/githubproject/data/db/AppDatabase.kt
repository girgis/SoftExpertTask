package com.example.githubproject.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product

@Database(entities = arrayOf(Product::class, FavoriteProducts::class), version = 1)
@TypeConverters(DataConverter::class)
abstract class AppDatabase:RoomDatabase() {
    abstract fun productDao() : ProductDao

    abstract fun favoriteProductsDao() : FavoriteProductDao
}