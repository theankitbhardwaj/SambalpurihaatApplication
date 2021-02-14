package com.inces.incesclient.models

data class MyOrderProduct(
    val description: String,
    val title: String,
    val variants: List<Variant>,
    val vendor_name: String
)