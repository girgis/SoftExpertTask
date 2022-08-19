package com.example.githubproject.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.githubproject.data.model.*
import com.example.githubproject.data.repository.MainRepository
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val mainRepository: MainRepository): ViewModel(){

    private val _users = MutableLiveData<Resource<WeatherModel>>()
    val users: LiveData<Resource<WeatherModel>> get() = _users

    private val _regions = MutableLiveData<Resource<List<Region>>>()
    val regions: LiveData<Resource<List<Region>>> get() = _regions

    init {
        fetchUsers("London")
    }

    fun fetchUsers(name: String){
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers("", name, 0, "", "").let {
                    if (it.isSuccessful) {
                        _users.postValue(Resource.success(it.body()))
                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }
    }

    fun fetchRegions(name: String){
        viewModelScope.launch {
            _regions.postValue(Resource.loading(null))
            if(networkHelper.isNetworkConnected()){
                mainRepository.getRegion("c4adb88e79e646ef95f213432221708", name).let {
                    if (it.isSuccessful){
                        _regions.postValue(Resource.success(it.body()))
                    }else {_regions.postValue(Resource.error(it.errorBody().toString(), null))}
                }

            }else _users.postValue(Resource.error("No internet connection", null))
        }
    }
}