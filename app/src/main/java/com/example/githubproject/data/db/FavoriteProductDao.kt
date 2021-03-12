package com.example.githubproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product

@Dao
interface FavoriteProductDao {
    @Query("SELECT * FROM favoriteProducts")
    fun getAll(): List<FavoriteProducts>

    @Query("SELECT * FROM favoriteProducts WHERE product_id IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<FavoriteProducts>

//    @Query("SELECT * FROM product WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): Product

    @Insert
    fun insertAll(vararg products: FavoriteProducts)

    @Insert
    fun insertAll(arrayList: ArrayList<FavoriteProducts>)

    @Delete
    fun delete(product: FavoriteProducts)
}