package com.nosa.posapp.features.store.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CartModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class ProductClickedViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                           private val mainRepository: MainRepository
): ViewModel() {

    private val _cart = MutableLiveData<Resource<CartModel>>()
    val cart: LiveData<Resource<CartModel>>
        get() = _cart

    fun createCart(lang: String, token:String, terminal_id: String, product_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.createCart(lang, token, terminal_id, product_id,"1").let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun addToCart(lang: String, token:String, terminal_id: String, product_id: String, session_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.addToCart(lang, token, terminal_id, product_id, session_id, "1").let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }
}