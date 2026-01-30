package com.example.adidusshop.models.responseModel



data class LoginResponse(
    val message: String,
    val login: Boolean,
    val user: UserResponse

)
