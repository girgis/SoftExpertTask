package com.example.githubproject.data.repository

import com.example.githubproject.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun getUsers(_key: String, _q:String, _days:Int, _aqi:String, _alerts: String) =
        apiHelper.getUsers(_key, _q, _days,_aqi, _alerts)

    suspend fun getRegion(_key: String, _q:String) = apiHelper.getRegion(_key, _q)
}