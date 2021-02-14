package com.inces.incesclient.models

import com.google.gson.annotations.SerializedName

data class ProductList<T>(
    val product: T,
    val items: List<CartVariantModel>
) {
    override fun toString(): String {
        return "ProductList(product=$product, items=$items)"
    }
}