package com.tradex.pos.features.returnOrders.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.githubproject.utils.NetworkHelper
import com.example.githubproject.utils.Resource
import com.tradex.pos.data.model.ActivationCodeModel
import com.tradex.pos.data.model.Product
import com.tradex.pos.data.model.RefundProduct
import com.tradex.pos.data.model.RefundProductModel
import com.tradex.pos.data.repository.MainRepository
import kotlinx.coroutines.launch


class ReturnOrderViewModel @ViewModelInject constructor(private val networkHelper: NetworkHelper,
                                                        private val mainRepository: MainRepository
): ViewModel(){

    private val _result = MutableLiveData<Resource<ActivationCodeModel>>()
    val result: LiveData<Resource<ActivationCodeModel>>
        get() = _result


    fun returnOrderRequest(lang: String, token: String, terminal_id: String, session_id: String, data: List<Product>){
        viewModelScope.launch {
            _result.postValue(Resource.loading(null))
            var returnedProductMap: List<RefundProduct> = data.map { RefundProduct(it.order_product_id, it.quantity, "return") }
            var refundProductModel: RefundProductModel = RefundProductModel(session_id, returnedProductMap)


            if (networkHelper.isNetworkConnected()){
                mainRepository.refundRequest(lang, token, terminal_id, refundProductModel).let {
                    if (it.isSuccessful) {
                        _result.postValue(Resource.success(it.body()))
                    } else _result.postValue(Resource.error(it.errorBody().toString(), null))
                }
            }else _result.postValue(Resource.error("No internet connection", null))
        }
    }

}