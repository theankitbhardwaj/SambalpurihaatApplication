package com.inces.incesclient.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CartVariantModel(
    @SerializedName("variant_id")
    @Expose
    var variantId: Int,
    @SerializedName("quantity")
    @Expose
    var quantity: Int
)