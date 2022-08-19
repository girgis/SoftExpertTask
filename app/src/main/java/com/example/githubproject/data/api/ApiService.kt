package com.example.githubproject.data.api

import com.example.githubproject.data.model.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("forecast.json")
    suspend fun getUsers(@Query("key") _key: String, @Query("q") _q: String, @Query("days") _days:Int,
            @Query("aqi") _aqi: String, @Query("alerts") _alerts: String): Response<WeatherModel>

    @GET("search.json")
    suspend fun getRegion(@Query("key") _key: String, @Query("q") _q: String): Response<List<Region>>
}