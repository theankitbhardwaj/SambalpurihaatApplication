package com.inces.incesclient.models

import com.google.gson.annotations.SerializedName


data class OtpResponse(
    @field:SerializedName("error") val isErr: Boolean,
    @field:SerializedName("message") val msg: String,
    @field:SerializedName("otp") val otp: String,
    @field:SerializedName("uid") val uid: String
)
