package com.inces.incesclient.models

import java.io.Serializable

data class Variant(
    val discount: Int,
    val image: List<Image>,
    val price: Int,
    val quantity: Int,
    var selectedQuantity: Int,
    var finalPrice: Int,
    val variant_id: Int,
    val variant_name: String
) : Serializable