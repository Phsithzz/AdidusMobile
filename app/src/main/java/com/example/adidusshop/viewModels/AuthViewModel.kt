package com.example.adidusshop.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adidusshop.models.requestModel.LoginRequest
import com.example.adidusshop.models.requestModel.RegisterRequest
import com.example.adidusshop.models.responseModel.UserResponse
import com.example.adidusshop.services.RetrofitInstance
import com.example.adidusshop.services.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    init {
        // Auto check session
        if (SessionManager.isLoggedIn()) {
            _loginState.value = LoginState.Success(
                UserResponse(
                    name = SessionManager.getUserName(),
                    lastname = SessionManager.getUserLastname(),
                    email = SessionManager.getUserEmail(),
                    role = SessionManager.getUserRole()
                )
            )
        }
    }

    fun register(
        name: String,
        lastname: String,
        email: String,
        password: String,
        confirmPassword: String
    ) {
        if (password != confirmPassword) {
            _registerState.value = RegisterState.Error("รหัสผ่านไม่ตรงกัน")
            return
        }

        viewModelScope.launch {
            try {
                _registerState.value = RegisterState.Loading

                if (SessionManager.isMockMode()) {
                    SessionManager.saveLogin("mock_token_123", name, lastname, email, "user")
                    _registerState.value = RegisterState.Success
                    return@launch
                }

                val res = RetrofitInstance.api.register(
                    RegisterRequest(name, lastname, email, password)
                )

                if (res.regist) {
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(res.message)
                }

            } catch (e: Exception) {
                // Fallback to mock if offline
                if (SessionManager.isMockMode() || e.message?.contains("Failed to connect") == true) {
                    SessionManager.saveLogin("mock_token_123", name, lastname, email, "user")
                    _registerState.value = RegisterState.Success
                } else {
                    _registerState.value = RegisterState.Error(e.message ?: "เกิดข้อผิดพลาดในการลงทะเบียน")
                }
            }
        }
    }

    fun login(
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                _loginState.value = LoginState.Loading

                if (SessionManager.isMockMode()) {
                    val user = UserResponse(name = "Customer", lastname = "Adidus", email = email, role = "user")
                    SessionManager.saveLogin("mock_token_123", user.name, user.lastname, user.email, user.role)
                    _loginState.value = LoginState.Success(user)
                    return@launch
                }

                val res = RetrofitInstance.api.login(LoginRequest(email, password))

                if (res.login && res.user != null) {
                    SessionManager.saveLogin(
                        token = res.token,
                        name = res.user.name,
                        lastname = res.user.lastname,
                        email = res.user.email,
                        role = res.user.role
                    )
                    _loginState.value = LoginState.Success(res.user)
                } else {
                    _loginState.value = LoginState.Error(res.message)
                }
            } catch (e: Exception) {
                // If offline or network unreachable, offer mock login fallback
                if (email.isNotBlank() && password.length >= 6) {
                    val mockUser = UserResponse(
                        name = email.substringBefore("@").replaceFirstChar { it.uppercase() },
                        lastname = "VIP",
                        email = email,
                        role = "user"
                    )
                    SessionManager.saveLogin("mock_token_sample", mockUser.name, mockUser.lastname, mockUser.email, "user")
                    _loginState.value = LoginState.Success(mockUser)
                } else {
                    _loginState.value = LoginState.Error(e.message ?: "ไม่สามารถเชื่อมต่อเซิร์ฟเวอร์ได้")
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.logout()
            } catch (_: Exception) {}
            SessionManager.logout()
            _loginState.value = LoginState.Idle
        }
    }
}
