package com.nosa.posapp.data.repository

import com.nosa.posapp.data.api.ApiHelper
import com.nosa.posapp.data.model.*
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Query
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun login(email: String, password: String, imei: String) = apiHelper.login(email, password, imei)

    suspend fun searchProductByBarcode(lang: String, token: String, terminal_id:String, barcode:String) =
        apiHelper.searchProductByBarcode(lang, token, terminal_id, barcode)

    suspend fun createCart(lang: String, token: String, terminal_id: String,
                                    product_id: String, for_stock: String): Response<CartModel> = apiHelper.createCart(lang, token, terminal_id, product_id, for_stock)

    suspend fun addToCart(lang: String, token: String, terminal_id: String,product_id:String,
                           session_id: String, for_stock: String): Response<CartModel> = apiHelper.addToCart(lang, token, terminal_id, product_id, session_id, for_stock)

    suspend fun incrementProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
                                 session_id: String): Response<CartModel> = apiHelper.incrementProduct(lang, token, terminal_id, cart_product_id, session_id)

    suspend fun decrementProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
                                 session_id: String): Response<CartModel> = apiHelper.decrementProduct(lang, token, terminal_id, cart_product_id, session_id)

    suspend fun deleteProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
                              session_id: String): Response<CartModel> = apiHelper.deleteProduct(lang, token, terminal_id, cart_product_id, session_id)

    suspend fun getCart(lang: String, token: String, terminal_id: String,
                              session_id: String): Response<CartModel> = apiHelper.getCart(lang, token, terminal_id, session_id)

    suspend fun makeOrder(lang: String, token: String, terminal_id:String, session_id:String, phone:String,
                          payment_method_id: String, transaction_id:String, card_number: String) : Response<Any> =
                            apiHelper.makeOrder(lang, token, terminal_id, session_id, phone, payment_method_id, transaction_id, card_number)

    suspend fun getCategories(lang: String, token: String, terminal_id: String, per_page: String): Response<CategoryModel>
            = apiHelper.getCategories(lang, token, terminal_id, per_page)

    suspend fun getCategoryProducts(lang: String, token: String, terminal_id:String, category_id: Int, page: Int): Response<ProductStoreModel>
            = apiHelper.getCategoryProducts(lang, token, terminal_id, category_id, page)

    suspend fun getSellHistory(lang: String, token: String, terminal_id: String, check_out_status: String, mFor: String, page: String): Response<SellHistoryModel>
            = apiHelper.getSellHistory(lang, token, terminal_id, check_out_status, mFor, page)

    suspend fun getInvoiceView(lang: String, token: String, terminal_id: String, session_id: String, phone: String): Response<InvoiceViewModel>
            = apiHelper.getInvoiceView(lang, token, terminal_id, session_id, phone)

    suspend fun searchOrdersAndCart(lang: String, token: String, terminal_id: String, order_id: String, _for: String): Response<CartModel>
            = apiHelper.searchOrdersAndCart(lang, token, terminal_id, order_id, _for)

    suspend fun getCode(lang: String, code: String): Response<ActivationCodeModel>
            = apiHelper.getCode(lang, code)

    suspend fun verifyCode(lang: String, phone: String, code: String): Response<ActivationCodeModel>
            = apiHelper.verifyCode(lang, phone, code)

    suspend fun resetPassword(lang: String, code: String, password: String, password_confirmation: String): Response<ActivationCodeModel>
            = apiHelper.resetPassword(lang, code, password, password_confirmation)

    suspend fun getSystemConstants(lang: String, token: String, terminal_id:String): Response<PaymentMethodsModel>
            = apiHelper.getSystemConstants(lang, token, terminal_id)
}