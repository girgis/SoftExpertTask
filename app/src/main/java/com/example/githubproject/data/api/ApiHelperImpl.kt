package com.example.githubproject.data.api

import com.example.githubproject.data.model.CarsModel
import com.example.githubproject.data.model.User
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper{

    override suspend fun getUsers(page: Int): Response<CarsModel> = apiService.getUsers(1)

}