package com.example.githubproject.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.githubproject.data.model.Product

@Dao
interface ProductDao {
    @Query("SELECT * FROM product")
    fun getAll(): List<Product>

    @Query("SELECT * FROM product WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<Product>

//    @Query("SELECT * FROM product WHERE first_name LIKE :first AND " +
//            "last_name LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): Product

    @Insert
    fun insertAll(vararg products: Product)

    @Insert
    fun insertAll(arrayList: ArrayList<Product>)

    @Delete
    fun delete(product: Product)
}