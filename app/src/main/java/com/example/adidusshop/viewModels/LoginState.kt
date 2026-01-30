package com.example.adidusshop.viewModels


import com.example.adidusshop.models.responseModel.UserResponse

sealed class LoginState {

    object Idle: LoginState()
    object Loading: LoginState()
    data class Success(val user: UserResponse): LoginState()
    data class Error(val message: String): LoginState()

}
