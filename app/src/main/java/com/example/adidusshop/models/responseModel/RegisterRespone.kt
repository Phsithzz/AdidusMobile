package com.example.adidusshop.models.responseModel

import com.example.adidusshop.models.User

data class RegisterResponse(
    val message: String,
    val user: User,
    val regist: Boolean

)
