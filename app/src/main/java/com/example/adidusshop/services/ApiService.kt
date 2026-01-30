package com.example.adidusshop.services

import com.example.adidusshop.models.User
import com.example.adidusshop.models.requestModel.LoginRequest
import com.example.adidusshop.models.requestModel.RegisterRequest
import com.example.adidusshop.models.responseModel.LoginResponse
import com.example.adidusshop.models.responseModel.RegisterResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    //test get
//    @GET("user")
//    suspend fun getAllUser():List<User>

    @POST("user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse



}

object RetrofitInstance {
    val api: ApiService by lazy{
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}