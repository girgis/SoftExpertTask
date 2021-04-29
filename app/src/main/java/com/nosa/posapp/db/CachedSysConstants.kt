package com.nosa.posapp.db

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nosa.posapp.data.model.PaymentMethods
import com.nosa.posapp.data.model.User

class CachedSysConstants (context: Context){
    private val mContext = context

    companion object{
        const val PREF_NAME: String = "sys_pref"
        const val PREF_SYS_CONSTANTS_KEY: String = "user_sys_constants_key"
    }

    private lateinit var pref: SharedPreferences

    fun saveSystemConstants(paymentMethods: PaymentMethods): Boolean{
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var paymentMethods_s = Gson().toJson(paymentMethods)
        return pref.edit().putString(PREF_SYS_CONSTANTS_KEY, paymentMethods_s).commit()
    }

    fun getSystemConstants(): PaymentMethods?{
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val paymentMethods = pref.getString(PREF_SYS_CONSTANTS_KEY, "")
        if (!paymentMethods.isNullOrEmpty()) {
            return Gson().fromJson(paymentMethods, PaymentMethods::class.java)
        }else return null
    }

    fun clearUser(){
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }
}