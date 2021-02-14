package com.inces.incesclient.models

import com.google.gson.JsonObject


data class Order(
    val orderDate: String,
    val orderID: Int,
    val orderStatus: String,
    val productList: JsonObject
)