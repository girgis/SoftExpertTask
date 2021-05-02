package com.tradex.pos.db

import com.tradex.pos.data.model.Cart
import javax.inject.Singleton

@Singleton
class CachedCart {
    var cart: Cart? = null

    companion object{
        private var instance: CachedCart? = null

        fun getInstance() : CachedCart {
            return when {
                instance != null -> instance!!
                else -> synchronized(this) {
                    if (instance == null) instance = CachedCart()
                    instance!!
                }
            }
        }
    }

    fun clearCart(){
        if (cart != null)
            cart = null
    }

}