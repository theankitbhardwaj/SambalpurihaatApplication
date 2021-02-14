package com.inces.incesclient.repo

import com.inces.incesclient.api.RetrofitClient
import com.inces.incesclient.models.OrderProduct
import com.inces.incesclient.models.User

class MainRepo {

    suspend fun createUser(user: User) = RetrofitClient.api.createUser(
        user.uid,
        user.fullname,
        user.phone_no,
        user.email,
        user.login_with
    )

    suspend fun getUser(uid: String) = RetrofitClient.api.getUser(uid)

    suspend fun getOtp(phone_no: String) = RetrofitClient.api.getOtp(phone_no)

    suspend fun getAllProducts() = RetrofitClient.api.getAllProducts()

    suspend fun getAllCategory() = RetrofitClient.api.getAllCategory()

    suspend fun orderProduct(productList: String, uid: String) =
        RetrofitClient.api.orderProduct(productList, uid)

    suspend fun getMyOrders(uid: String) =
        RetrofitClient.api.getMyOrders(uid)


    suspend fun cancelOrder(orderId: Int) = RetrofitClient.api.cancelOrder(orderId)

    suspend fun promotedProducts() = RetrofitClient.api.promotionalProducts()


}