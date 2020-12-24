package com.example.githubproject.data.repository

import com.example.githubproject.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper){
    suspend fun getUsers(page: Int) = apiHelper.getUsers(page)
}