package com.inces.incesclient.models

data class MyOrderProduct(
        val base_image:String,
        val description: String,
        val title: String,
        val variants: List<Variant>,
        val vendor_name: String
)