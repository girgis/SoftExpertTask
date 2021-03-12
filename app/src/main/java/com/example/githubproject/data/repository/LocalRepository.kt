package com.example.githubproject.data.repository

import android.util.Log
import com.example.githubproject.data.db.AppDatabase
import com.example.githubproject.data.db.FavoriteProductDao
import com.example.githubproject.data.db.ProductDao
import com.example.githubproject.data.model.Product
import javax.inject.Inject

class LocalRepository @Inject constructor(private val db: ProductDao, private val fdb: FavoriteProductDao){
    suspend fun getLocalProducts() = db.getAll()

    suspend fun addDefaultProducts(){
        if (db.getAll().isNullOrEmpty()){
            Log.d("LocalRep", "isNullOrEmpty()")
            var mList: ArrayList<Product> = arrayListOf()
            for (i in 1..7){
                val product:Product = Product(null, "Product ".plus(i), "Product_img_".plus(i))
                mList.add(product)
            }
            db.insertAll(mList)
        }
    }

    suspend fun getAllFavoriteProducts() = fdb.getAll()

    suspend fun addToFavorite(product: Product){

    }
}