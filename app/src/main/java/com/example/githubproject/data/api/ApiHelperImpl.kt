package com.example.githubproject.data.api

import com.example.githubproject.data.model.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper{

    override suspend fun getUsers(_key: String, _q:String, _days:Int,_aqi:String, _alerts: String): Response<WeatherModel> =
        apiService.getUsers("c4adb88e79e646ef95f213432221708", _q, 3,"no", "no")

    override suspend fun getRegion(_key: String, _q: String): Response<List<Region>> = apiService.getRegion(_key, _q)

}