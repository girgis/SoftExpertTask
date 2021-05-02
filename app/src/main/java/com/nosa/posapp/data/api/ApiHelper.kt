package com.nosa.posapp.data.api

import com.nosa.posapp.data.model.*
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiHelper {

    suspend fun login(email: String, password: String, imei: String):Response<UserModel>

    suspend fun searchProductByBarcode(lang: String, token: String, terminal_id:String, barcode:String): Response<ProductModel>

    suspend fun createCart(lang: String, token: String, terminal_id:String, product_id:String, for_stock: String): Response<CartModel>

    suspend fun addToCart(lang: String, token: String, terminal_id:String, product_id:String, session_id:String, for_stock: String): Response<CartModel>

    suspend fun incrementProduct(lang: String, token: String, terminal_id:String, cart_product_id:String, session_id:String): Response<CartModel>

    suspend fun decrementProduct(lang: String, token: String, terminal_id:String, cart_product_id:String, session_id:String): Response<CartModel>

    suspend fun deleteProduct(lang: String, token: String, terminal_id:String, cart_product_id:String, session_id:String): Response<CartModel>

    suspend fun getCart(lang: String, token: String, terminal_id:String, session_id:String): Response<CartModel>

    suspend fun makeOrder(lang: String, token: String, terminal_id:String, session_id:String, phone:String,
                          payment_method_id: String, transaction_id:String, card_number: String) : Response<Any>

    suspend fun getCategories(lang: String, token: String, terminal_id:String, per_page: String) : Response<CategoryModel>

    suspend fun getCategoryProducts(lang: String, token: String, terminal_id:String, category_id: Int, page: Int): Response<ProductStoreModel>

    suspend fun getSellHistory(lang: String, token: String, terminal_id:String,
                                           check_out_status: String, mFor: String, page: String): Response<SellHistoryModel>

    suspend fun getInvoiceView(lang: String, token: String, terminal_id:String, session_id: String, phone: String): Response<InvoiceViewModel>

    suspend fun searchOrdersAndCart(lang: String, token: String, terminal_id:String, order_id: String, _for: String): Response<CartModel>

    suspend fun getCode(lang: String, phone: String): Response<ActivationCodeModel>

    suspend fun verifyCode(lang: String, phone: String, code: String): Response<ActivationCodeModel>

    suspend fun resetPassword(lang: String, code: String, password: String, password_confirmation: String): Response<ActivationCodeModel>

    suspend fun getSystemConstants(lang: String, token: String, terminal_id:String): Response<PaymentMethodsModel>

    suspend fun refundRequest(lang: String, token: String, terminal_id: String, body: RefundProductModel): Response<ActivationCodeModel>
}