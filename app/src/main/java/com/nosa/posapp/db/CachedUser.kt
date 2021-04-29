package com.nosa.posapp.db

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.nosa.posapp.data.model.PaymentMethods
import com.nosa.posapp.data.model.User

class CachedUser (context: Context){
    private val mContext = context

    companion object{
        const val PREF_NAME: String = "user_pref"
        const val PREF_KEY: String = "user_pref_key"
        const val PREF_SYS_CONSTANTS_KEY: String = "user_sys_constants_key"
    }

    private lateinit var pref: SharedPreferences

    fun saveUser(user: User): Boolean{
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        var user_s = Gson().toJson(user)
        return pref.edit().putString(PREF_KEY, user_s).commit()
    }

    fun getUser(): User?{
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val user = pref.getString(PREF_KEY, "")
        if (!user.isNullOrEmpty()) {
            return Gson().fromJson(user, User::class.java)
        }else return null
    }

    fun clearUser(){
        pref = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().clear().apply()
    }
}