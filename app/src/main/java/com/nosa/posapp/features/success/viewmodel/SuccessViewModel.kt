package com.nosa.posapp.features.success.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.nosa.posapp.data.model.CategoryModel
import com.nosa.posapp.data.model.InvoiceViewModel
import com.nosa.posapp.data.repository.MainRepository
import kotlinx.coroutines.launch

class SuccessViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                    private val mainRepository: MainRepository
): ViewModel(){

    private val _result = MutableLiveData<Resource<InvoiceViewModel>>()
    val result: LiveData<Resource<InvoiceViewModel>>
        get() = _result


    fun getInvoiceView(lang: String, token: String, terminal_id: String, sessionId: String, phone: String){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()){
                mainRepository.getInvoiceView(lang, token, terminal_id, sessionId, phone).let {
                    if (it.isSuccessful) {
                        _result.postValue(Resource.success(it.body()))
                    } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }

}