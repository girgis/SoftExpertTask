package com.example.githubproject.features.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubproject.data.model.FavoriteProducts
import com.example.githubproject.data.model.Product
import com.example.githubproject.data.repository.LocalRepository
import com.example.githubproject.data.repository.MainRepository
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CartViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val mainRepository: MainRepository,
    private val localRepository: LocalRepository
): ViewModel(){

    private val _products = MutableLiveData<Resource<List<FavoriteProducts>>>()
    val products: LiveData<Resource<List<FavoriteProducts>>> get() = _products

    fun fetchProducts(){
        GlobalScope.launch(Dispatchers.IO) {
            _products.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                localRepository.getAllFavoriteProducts().let {
                    if (it != null) {
                        _products.postValue(Resource.success(it))
                    } else _products.postValue(Resource.error("Error", null))
                }
            } else _products.postValue(Resource.error("No internet connection", null))
        }
    }
}