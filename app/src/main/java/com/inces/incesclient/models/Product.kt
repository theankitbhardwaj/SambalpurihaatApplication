package com.inces.incesclient.models

import java.io.Serializable

data class Product(
    val base_name: String,
    val description: String,
    val main_category: String,
    val product_id: String,
    val publish_date: String,
    val sub_category: String,
    val supplier_name: String,
    val terms_condition: String,
    val title: String,
    val category: String,
    var variants: List<Variant>,
) : Serializable {
    override fun toString(): String {
        return "Product(description='$description', product_id='$product_id', save_date='$publish_date', title='$title', variants=$variants, vendor_name='$supplier_name')"
    }
}
