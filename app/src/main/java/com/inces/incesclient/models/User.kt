package com.inces.incesclient.models

import java.io.Serializable

data class User(
    var email: String,
    var fullname: String,
    var login_with: String,
    var phone_no: String,
    var uid: String
) : Serializable