package com.inces.incesclient.models

data class OrderProduct(
    val productId: String,
    val variantIds: List<String>
) {
}