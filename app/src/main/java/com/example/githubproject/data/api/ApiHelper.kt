package com.example.githubproject.data.api

import com.example.githubproject.data.model.CarsModel
import com.example.githubproject.data.model.User
import retrofit2.Response

interface ApiHelper {
    suspend fun getUsers(page: Int): Response<CarsModel>
}