package com.nosa.posapp.features.login

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.UserModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                      private val mainRepository: MainRepository): ViewModel(){

    private val _user = MutableLiveData<Resource<UserModel>>()
    val user: LiveData<Resource<UserModel>>
        get() =_user

    fun login(email: String, password: String, imei: String){
        viewModelScope.launch {
            _user.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                mainRepository.login(email, password, imei).let {
                    if (it.isSuccessful){
                        _user.postValue(Resource.success(it.body()))
                    } else _user.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _user.postValue(Resource.error("No internet connection", null))
        }
    }

}