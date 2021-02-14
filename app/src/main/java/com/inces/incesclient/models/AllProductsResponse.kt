package com.inces.incesclient.models

data class AllProductsResponse(
    val error: Boolean,
    val products: List<Product>
)