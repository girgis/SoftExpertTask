package com.nosa.posapp.features.store.products.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CategoryModel
import com.nosa.posapp.data.model.ProductModel
import com.nosa.posapp.data.model.ProductStoreModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class StoreProductsViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                          private val mainRepository: MainRepository
): ViewModel() {

    private val _result = MutableLiveData<Resource<ProductStoreModel>>()
    val result: LiveData<Resource<ProductStoreModel>>
        get() = _result

    fun getCategories(lang: String, token:String, terminal_id: String, category_id: Int, page: Int){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getCategoryProducts(lang, token, terminal_id,  category_id, page).let {
                        if (it.isSuccessful) {
                            _result.postValue(Resource.success(it.body()))
                        } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _result.postValue(Resource.error("System Erroe", null))
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }
}