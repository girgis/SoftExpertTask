package com.example.githubproject.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.githubproject.data.model.CarsModel
import com.example.githubproject.data.model.User
import com.example.githubproject.data.repository.MainRepository
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val mainRepository: MainRepository): ViewModel(){

    private val _users = MutableLiveData<Resource<CarsModel>>()
    val users: LiveData<Resource<CarsModel>> get() = _users

    init {
        fetchUsers(1)
    }

    fun fetchUsers(page: Int){
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
            Log.d("zzzz", "--- ${page}")
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getUsers(page).let {
                    if (it.isSuccessful) {
                        _users.postValue(Resource.success(it.body()))
                    } else _users.postValue(Resource.error(it.errorBody().toString(), null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }
    }
}