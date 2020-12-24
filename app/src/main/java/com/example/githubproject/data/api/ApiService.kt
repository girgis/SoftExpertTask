package com.example.githubproject.data.api

import com.example.githubproject.data.model.CarsModel
import com.example.githubproject.data.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("cars")
    suspend fun getUsers(@Query("page") page: Int): Response<CarsModel>

}