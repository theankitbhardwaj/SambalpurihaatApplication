package com.inces.incesclient.api

import com.inces.incesclient.models.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface Api {

    @FormUrlEncoded
    @POST("customer/createuser")
    suspend fun createUser(
        @Field("uid") uid: String,
        @Field("fullname") fullName: String,
        @Field("phone_no") phoneNo: String,
        @Field("email") email: String,
        @Field("login_with") loginWith: String
    ): Response<GenericResponse>


    @GET("customer/getuser/{uid}")
    suspend fun getUser(
        @Path("uid") uid: String
    ): Response<GetUserResponse>


    @GET("getallproducts")
    suspend fun getAllProducts(): Response<AllProductsResponse>

    @GET("getallcategory")
    suspend fun getAllCategory(): Response<CategoryImageModel>

    @FormUrlEncoded
    @POST("customer/ordernow")
    suspend fun orderProduct(
        @Field("productList") productList: String,
        @Field("uid") uid: String
    ): Response<GenericResponse>

    @FormUrlEncoded
    @POST("customer/sendotp")
    fun getOtp(
        @Field("phone_no") phone_no: String?
    ): Call<OtpResponse?>

    @GET("customer/getorders/{uid}")
    suspend fun getMyOrders(@Path("uid") uid: String): Response<MyOrderResponse>

    @GET("customer/cancelorder/{orderid}")
    suspend fun cancelOrder(@Path("orderid") orderId: Int): Response<GenericResponse>

    @GET("vendor/promotional_suppliers")
    suspend fun promotionalProducts(): Response<PromotionalResponse>




}