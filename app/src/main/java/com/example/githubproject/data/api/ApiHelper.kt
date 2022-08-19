package com.example.githubproject.data.api

import com.example.githubproject.data.model.*
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(_key: String, _q:String, _days:Int, _aqi:String, _alerts: String): Response<WeatherModel>

    suspend fun getRegion(_key: String, _q:String) : Response<List<Region>>
}