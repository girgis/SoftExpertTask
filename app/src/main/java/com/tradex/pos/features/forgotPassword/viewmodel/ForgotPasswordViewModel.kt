package com.tradex.pos.features.forgotPassword.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.tradex.pos.data.model.ActivationCodeModel
import com.tradex.pos.data.repository.MainRepository
import kotlinx.coroutines.launch

class ForgotPasswordViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                           private val mainRepository: MainRepository): ViewModel(){

    private val _result = MutableLiveData<Resource<ActivationCodeModel>>()
    val result: LiveData<Resource<ActivationCodeModel>>
        get() =_result

    private val _verify_result = MutableLiveData<Resource<ActivationCodeModel>>()
    val verify_result: LiveData<Resource<ActivationCodeModel>>
        get() =_verify_result

    private val _reset_result = MutableLiveData<Resource<ActivationCodeModel>>()
    val reset_result: LiveData<Resource<ActivationCodeModel>>
        get() =_reset_result

    fun getActivationCode(lang: String, code: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                mainRepository.getCode(lang, code).let {
                    if (it.isSuccessful){
                        _result.postValue(Resource.success(it.body()))
                    } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }

    fun verifyCode(lang: String, phone: String, code: String){
        viewModelScope.launch {
            _verify_result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                mainRepository.verifyCode(lang, phone, code).let {
                    if (it.isSuccessful){
                        _verify_result.postValue(Resource.success(it.body()))
                    } else _verify_result.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _verify_result.postValue(Resource.error("No internet connection", null))
        }
    }

    fun resetPassword(lang: String, code: String, password: String, reset_password: String){
        viewModelScope.launch {
            _reset_result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                mainRepository.resetPassword(lang, code, password, reset_password).let {
                    if (it.isSuccessful){
                        _reset_result.postValue(Resource.success(it.body()))
                    } else _reset_result.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _reset_result.postValue(Resource.error("No internet connection", null))
        }
    }

}