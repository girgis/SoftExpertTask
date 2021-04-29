package com.nosa.posapp.features.store.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CategoryModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class StoreViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                  private val mainRepository: MainRepository
): ViewModel() {

    private val _result = MutableLiveData<Resource<CategoryModel>>()
    val result: LiveData<Resource<CategoryModel>>
        get() = _result

    fun getCategories(lang: String, token:String, terminal_id: String, per_page: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getCategories(lang, token, terminal_id, per_page).let {
                        if (it.isSuccessful) {
                            _result.postValue(Resource.success(it.body()))
                        } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _result.postValue(Resource.error("System Erroe", null))
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }
}