package com.example.githubproject.ui.main.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.example.githubproject.data.db.AppDatabase
import com.example.githubproject.data.model.CarsModel
import com.example.githubproject.data.model.Product
import com.example.githubproject.data.model.ProductsModel
import com.example.githubproject.data.model.User
import com.example.githubproject.data.repository.LocalRepository
import com.example.githubproject.data.repository.MainRepository
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class MainViewModel @ViewModelInject constructor(
    private val networkHelper: NetworkHelper,
    private val mainRepository: MainRepository,
    private val localRepository: LocalRepository): ViewModel(){

    @Inject lateinit var db: AppDatabase

    private val _users = MutableLiveData<Resource<CarsModel>>()
    val users: LiveData<Resource<CarsModel>> get() = _users

    private val _products = MutableLiveData<Resource<List<Product>>>()
    val products: LiveData<Resource<List<Product>>> get() = _products

    init {
//        fetchUsers(1)
        setDefaultProductAtFirstTime()
        removeExpired()
        fetchProducts()
    }

    private fun setDefaultProductAtFirstTime(){
        GlobalScope.launch(Dispatchers.IO) {
            localRepository.addDefaultProducts()
        }
    }

    private fun removeExpired(){
        GlobalScope.launch(Dispatchers.IO) {
            var list = localRepository.getAllFavoriteProducts()
            if(!list.isNullOrEmpty()){
                for(fp in list ){
                    val calendar = Calendar.getInstance()
                    calendar.time = fp.updatedAt
                    calendar.add(Calendar.DAY_OF_YEAR, 3)
                    val diff = calendar.timeInMillis - Calendar.getInstance().timeInMillis
                    if (diff <= 0){
                        db.favoriteProductsDao().delete(fp)
                    }
                }
            }
        }
    }

    fun fetchProducts(){
        GlobalScope.launch(Dispatchers.IO) {
            delay(1000)
            _products.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                localRepository.getLocalProducts().let {
                    if (it.isNotEmpty()) {
                        _products.postValue(Resource.success(it))
                    } else _products.postValue(Resource.error("Error", null))
                }
            } else _users.postValue(Resource.error("No internet connection", null))
        }
    }

    fun fetchUsers(page: Int){
        viewModelScope.launch {
            _users.postValue(Resource.loading(null))
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