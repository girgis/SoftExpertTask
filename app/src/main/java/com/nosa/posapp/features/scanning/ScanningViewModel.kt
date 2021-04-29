package com.nosa.posapp.features.scanning

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CartModel
import com.nosa.posapp.data.model.ProductModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class ScanningViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                     private val mainRepository: MainRepository
): ViewModel(){

    private val _product = MutableLiveData<Resource<ProductModel>>()
    val product: LiveData<Resource<ProductModel>>
        get() = _product

    private val _cart = MutableLiveData<Resource<CartModel>>()
    val cart: LiveData<Resource<CartModel>>
        get() = _cart

    private val _result = MutableLiveData<Resource<Any>>()
    val result: LiveData<Resource<Any>>
        get() = _result

    fun searchProductsByBarcode(lang: String, token:String, terminal_id:String, barcode: String){
        viewModelScope.launch {
            _product.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.searchProductByBarcode(lang, token, terminal_id, barcode).let {
                        if (it.isSuccessful) {
                            _product.postValue(Resource.success(it.body()))
                        } else _product.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _product.postValue(Resource.error("System Erroe", null))
            }else _product.postValue(Resource.error("No internet connection", null))
        }
    }

    fun createCart(lang: String, token:String, terminal_id: String, product_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.createCart(lang, token, terminal_id, product_id,"0").let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getCart(lang: String, token:String, terminal_id: String, session_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getCart(lang, token, terminal_id, session_id).let {
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
            _product.postValue(Resource.loading(null))
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

    fun incrementProductByID(lang: String, token:String, terminal_id: String, product_id: String, session_id: String){
        viewModelScope.launch {
            _product.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.incrementProduct(lang, token, terminal_id, product_id, session_id).let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun decrementProductByID(lang: String, token:String, terminal_id: String, product_id: String, session_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.decrementProduct(lang, token, terminal_id, product_id, session_id).let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun deleteProductByID(lang: String, token:String, terminal_id: String, product_id: String, session_id: String){
        viewModelScope.launch {
            _cart.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.deleteProduct(lang, token, terminal_id, product_id, session_id).let {
                        if (it.isSuccessful) {
                            _cart.postValue(Resource.success(it.body()))
                        } else _cart.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _cart.postValue(Resource.error("System Erroe", null))
            }else _cart.postValue(Resource.error("No internet connection", null))
        }
    }

    fun makeOrder(lang: String, token:String, terminal_id: String, session_id: String, phone: String, payment_method_id: String,
                  transaction_id: String, card_number: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.makeOrder(lang, token, terminal_id, session_id, phone, payment_method_id, transaction_id, card_number).let {
                        if (it.isSuccessful) {
                            _result.postValue(Resource.success(it.body()))
                        } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _result.postValue(Resource.error("System Erroe", null))
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }

}