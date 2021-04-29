package com.nosa.posapp.features.main

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CartModel
import com.nosa.posapp.data.model.PaymentMethodsModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                 private val mainRepository: MainRepository
): ViewModel() {

    private val _cart = MutableLiveData<Resource<CartModel>>()
    val cart: LiveData<Resource<CartModel>>
        get() = _cart

    private val _system = MutableLiveData<Resource<PaymentMethodsModel>>()
    val system: LiveData<Resource<PaymentMethodsModel>>
        get() = _system

    fun searchOrders(lang: String, token:String, terminal_id: String, id: String, _for: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.searchOrdersAndCart(lang,  token, terminal_id, id, _for).let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System error", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getSystemConstrains(lang: String, token:String, terminal_id: String){
        viewModelScope.launch {
            _system.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getSystemConstants(lang,  token, terminal_id).let {
                        if (it.isSuccessful) {
                            _system.postValue(Resource.success(it.body()))
                        } else _system.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _system.postValue(Resource.error("System error", null))
            }else _system.postValue(Resource.error("No internet connection", null))
        }
    }

}