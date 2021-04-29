package com.nosa.posapp.data.api

import com.nosa.posapp.data.model.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService): ApiHelper {

    override suspend fun login(email: String, password: String, imei: String) = apiService.login(email, password, imei)

    override suspend fun searchProductByBarcode(lang: String, token: String, terminal_id: String,
                                                barcode: String): Response<ProductModel> = apiService.searchProductByBarcode(lang, token, terminal_id, barcode)

    override suspend fun createCart(lang: String, token: String, terminal_id: String,
                                    product_id: String, for_stock: String): Response<CartModel> = apiService.createCart(lang, token, terminal_id, product_id, for_stock)

    override suspend fun addToCart(lang: String, token: String, terminal_id: String, product_id:String,
                                    session_id: String, for_stock: String): Response<CartModel> = apiService.addToCart(lang, token, terminal_id, product_id,
        session_id, for_stock)

    override suspend fun incrementProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
        session_id: String): Response<CartModel> {
        return apiService.incrementProduct(lang, token, terminal_id, cart_product_id, session_id)
    }

    override suspend fun decrementProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
        session_id: String): Response<CartModel> {
        return apiService.decrementProduct(lang, token, terminal_id, cart_product_id, session_id)
    }

    override suspend fun deleteProduct(lang: String, token: String, terminal_id: String, cart_product_id: String,
        session_id: String): Response<CartModel>  = apiService.deleteProduct(lang, token, terminal_id, cart_product_id, session_id)

    override suspend fun getCart(lang: String, token: String, terminal_id: String, session_id: String): Response<CartModel> =
        apiService.getCart(lang, token, terminal_id, session_id)

    override suspend fun makeOrder(lang: String, token: String, terminal_id: String, session_id: String, phone: String,
        payment_method_id: String, transaction_id: String, card_number: String): Response<Any> =
        apiService.makeOrder(lang, token, terminal_id, session_id, phone, payment_method_id, transaction_id, card_number)

    override suspend fun getCategories(lang: String, token: String, terminal_id: String, per_page: String): Response<CategoryModel> = apiService.getCategories(lang, token, terminal_id, per_page)

    override suspend fun getCategoryProducts(lang: String, token: String, terminal_id: String, category_id: Int, page: Int): Response<ProductStoreModel>
            = apiService.getCategoryProducts(lang, token, terminal_id, category_id, page)

    override suspend fun getSellHistory(lang: String, token: String, terminal_id: String, check_out_status: String, mFor: String, page: String): Response<SellHistoryModel>
            = apiService.getSellHistory(lang, token, terminal_id, check_out_status, mFor, page)

    override suspend fun getInvoiceView(lang: String, token: String, terminal_id:String, session_id: String, phone: String): Response<InvoiceViewModel>
            = apiService.getInvoiceView(lang, token, terminal_id, session_id, phone)

    override suspend fun searchOrdersAndCart(lang: String, token: String, terminal_id: String, order_id: String, _for: String): Response<CartModel>
            = apiService.searchOrdersAndCart(lang, token, terminal_id, order_id, _for)

    override suspend fun getCode(lang: String, phone: String): Response<ActivationCodeModel> = apiService.getCode(lang, phone)

    override suspend fun verifyCode(lang: String, phone: String, code: String): Response<ActivationCodeModel>
            = apiService.verifyCode(lang, phone, code)

    override suspend fun resetPassword(lang: String, code: String, password: String, password_confirmation: String): Response<ActivationCodeModel>
            = apiService.resetPassword(lang, code, password, password_confirmation)

    override suspend fun getSystemConstants(lang: String, token: String, terminal_id:String): Response<PaymentMethodsModel>
            = apiService.getSystemConstants(lang, token, terminal_id)

}