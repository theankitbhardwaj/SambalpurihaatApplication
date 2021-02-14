package com.inces.incesclient.models

data class GetUserResponse(
    val error: Boolean,
    val message: String,
    val user: User?
)