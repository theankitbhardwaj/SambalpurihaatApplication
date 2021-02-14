package com.inces.incesclient.models

data class MyOrderResponse(
    val error: Boolean,
    val orders: List<Order>
)