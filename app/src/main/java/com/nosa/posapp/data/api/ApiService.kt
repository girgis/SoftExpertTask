package com.nosa.posapp.data.api

import com.nosa.posapp.data.model.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("login")
    suspend fun login(@Field("email") email: String, @Field("password") password: String,
                      @Field("terminal_id") terminal_id: String): Response<UserModel>

    @GET("barcode")
    suspend fun searchProductByBarcode(@Header("X-localization") lang: String,
        @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                                       @Query("barcode") barcode:String): Response<ProductModel>

    @FormUrlEncoded
    @POST("cart")
    suspend fun createCart(@Header("X-localization") lang: String,
                           @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                           @Field("product_id") product_id:String, @Field("for_stock") for_stock:String): Response<CartModel>

    @FormUrlEncoded
    @POST("cart")
    suspend fun addToCart(@Header("X-localization") lang: String,
                          @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                          @Field("product_id") product_id:String, @Field("session_id") session_id:String,
                          @Field("for_stock") for_stock:String): Response<CartModel>

    @FormUrlEncoded
    @POST("cart/increment")
    suspend fun incrementProduct(@Header("X-localization") lang: String,
                          @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                          @Field("cart_product_id") cart_product_id:String, @Field("session_id") session_id:String): Response<CartModel>

    @FormUrlEncoded
    @POST("cart/decrement")
    suspend fun decrementProduct(@Header("X-localization") lang: String,
                                  @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                                  @Field("cart_product_id") cart_product_id:String, @Field("session_id") session_id:String): Response<CartModel>

    @DELETE("cart")
    suspend fun deleteProduct(@Header("X-localization") lang: String,
                              @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                              @Query("cart_product_id") cart_product_id:String, @Query("session_id") session_id:String): Response<CartModel>

    @GET("cart")
    suspend fun getCart(@Header("X-localization") lang: String, @Header("api-token") token: String,
                        @Header("terminal-id") terminal_id:String, @Query("session_id") session_id:String): Response<CartModel>

    @FormUrlEncoded
    @POST("orders")
    suspend fun makeOrder(@Header("X-localization") lang: String,
                           @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                           @Field("session_id") session_id:String, @Field("phone") phone:String,
                           @Field("payment_method_id") payment_method_id: String, @Field("transaction_id") transaction_id:String,
                           @Field("card_number") card_number: String): Response<Any>

    @GET("categories/paginate")
    suspend fun getCategories(@Header("X-localization") lang: String, @Header("api-token") token: String,
                              @Header("terminal-id") terminal_id:String, @Query("page") per_page: String): Response<CategoryModel>

//    @GET("brand/products")
    @GET("products/paginate")
    suspend fun getCategoryProducts(@Header("X-localization") lang: String,
                                    @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                                    @Query("category_id") category_id: Int, @Query("page") page: Int): Response<ProductStoreModel>

    @GET("orders/byFilter")
    suspend fun getSellHistory(@Header("X-localization") lang: String,
                                    @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                                    @Query("check_out_status") check_out_status: String, @Query("for") mFor: String,
                                    @Query("page") page: String): Response<SellHistoryModel>

    @GET("orders/invoice/link")
    suspend fun getInvoiceView(@Header("X-localization") lang: String,
                               @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                               @Query("session_id") session_id: String, @Query("phone", encoded = true) phone: String):Response<InvoiceViewModel>

    @GET("orders/search")
    suspend fun searchOrdersAndCart(@Header("X-localization") lang: String,
                                    @Header("api-token") token: String, @Header("terminal-id") terminal_id:String,
                                    @Query("order_id") order_id: String, @Query("for") _for: String): Response<CartModel>

    @GET("code")
    suspend fun getCode(@Header("X-localization") lang: String, @Query("phone", encoded = true) phone: String): Response<ActivationCodeModel>

    @GET("system")
    suspend fun getSystemConstants(@Header("X-localization") lang: String, @Header("api-token") token: String,
                                   @Header("terminal-id") terminal_id:String): Response<PaymentMethodsModel>


    @POST("code")
    @FormUrlEncoded
    suspend fun verifyCode(@Header("X-localization") lang: String, @Field("phone") phone: String,
                           @Field("code") code: String): Response<ActivationCodeModel>

    @POST("resetPassword")
    @FormUrlEncoded
    suspend fun resetPassword(@Header("X-localization") lang: String, @Field("code") code: String, @Field("password") password: String,
                              @Field("password_confirmation") password_confirmation: String): Response<ActivationCodeModel>

}