package com.inces.incesclient.models

data class PromotionalResponse(
    val error: Boolean,
    val suppliers: List<Supplier>
)