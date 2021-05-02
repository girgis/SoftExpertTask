package com.tradex.pos.features.ordersHistory.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.tradex.pos.data.model.SellHistoryModel
import com.tradex.pos.data.repository.MainRepository
import kotlinx.coroutines.launch

class SellHistoryViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                        private val mainRepository: MainRepository
): ViewModel() {

    private val _result = MutableLiveData<Resource<SellHistoryModel>>()
    val result: LiveData<Resource<SellHistoryModel>>
        get() = _result

    fun getSellHistory(lang: String, token:String, terminal_id: String, page: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getSellHistory(lang, token, terminal_id, "all", "sell", page).let {
                        if (it.isSuccessful) {
                            _result.postValue(Resource.success(it.body()))
                        } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _result.postValue(Resource.error("System Erroe", null))
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }

    fun getStokeHistory(lang: String, token:String, terminal_id: String, page: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                if(!token.isNullOrEmpty()) {
                    mainRepository.getSellHistory(lang, token, terminal_id, "all", "stock", page).let {
                        if (it.isSuccessful) {
                            _result.postValue(Resource.success(it.body()))
                        } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                    }
                }else _result.postValue(Resource.error("System Erroe", null))
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }
}