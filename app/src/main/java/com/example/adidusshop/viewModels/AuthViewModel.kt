package com.example.adidusshop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adidusshop.models.requestModel.LoginRequest
import com.example.adidusshop.models.requestModel.RegisterRequest
import com.example.adidusshop.services.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel: ViewModel(){

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState : StateFlow<LoginState> = _loginState

    fun register(
        name:String,
        lastname:String,
        email:String,
        password:String,
        confirmPassword:String
    ){
        if(password !=confirmPassword){
            _registerState.value = RegisterState.Error("Password not Match")
            return
        }

        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading
                val res = RetrofitInstance.api.register(
                    RegisterRequest(name,lastname,email,password)
                )

                if (res.regist){
                    _registerState.value = RegisterState.Success
                }
                else{
                    _registerState.value = RegisterState.Error(res.message)
                }

            }
            catch (e: Exception){
                _registerState.value = RegisterState.Error(e.message?:"Error")
            }
        }

    }

    fun login(
        email:String,
        password:String
    ){
        viewModelScope.launch{
            try {
                _loginState.value = LoginState.Loading
                val res = RetrofitInstance.api.login(LoginRequest(email,password))

                if(res.login){
                    _loginState.value = LoginState.Success(res.user)
                }
                else{
                    _loginState.value = LoginState.Error(res.message)
                }
            }
            catch (e: Exception){
                _loginState.value = LoginState.Error(e.message?:"Error")
            }
        }
    }
}
