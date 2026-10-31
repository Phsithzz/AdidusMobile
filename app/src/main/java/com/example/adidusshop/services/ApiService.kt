package com.example.adidusshop.services

import com.example.adidusshop.models.CartItem
import com.example.adidusshop.models.OrderItem
import com.example.adidusshop.models.Product
import com.example.adidusshop.models.requestModel.AddCartRequest
import com.example.adidusshop.models.requestModel.CheckoutRequest
import com.example.adidusshop.models.requestModel.LoginRequest
import com.example.adidusshop.models.requestModel.RegisterRequest
import com.example.adidusshop.models.requestModel.UpdateCartQuantityRequest
import com.example.adidusshop.models.responseModel.AddCartResponse
import com.example.adidusshop.models.responseModel.LoginResponse
import com.example.adidusshop.models.responseModel.RegisterResponse
import com.example.adidusshop.models.responseModel.UserResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    // ==================== AUTH ====================
    @POST("user/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): RegisterResponse

    @POST("user/login")
    suspend fun login(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("user/logout")
    suspend fun logout(): Map<String, Any>

    @GET("user/me")
    suspend fun getMe(): UserResponse

    // ==================== PRODUCTS ====================
    @GET("products")
    suspend fun getProduct(): List<Product>

    @GET("products/type/{description}")
    suspend fun getProductType(
        @Path("description") description: String
    ): List<Product>

    @GET("products/{product_id}")
    suspend fun getProductId(
        @Path("product_id") product_id: Int
    ): Product

    @GET("products/search")
    suspend fun searchProducts(
        @Query("q") query: String
    ): List<Product>

    // ==================== CART ====================
    @GET("cart")
    suspend fun getCart(): List<CartItem>

    @POST("cart/items")
    suspend fun addCart(
        @Body request: AddCartRequest
    ): AddCartResponse

    @PUT("cart/items/{cartId}")
    suspend fun updateCartQuantity(
        @Path("cartId") cartId: Int,
        @Body request: UpdateCartQuantityRequest
    ): CartItem

    @DELETE("cart/items/{cartId}")
    suspend fun removeCartItem(
        @Path("cartId") cartId: Int
    ): Map<String, Any>

    // ==================== CHECKOUT & ORDERS ====================
    @POST("cart/checkout")
    suspend fun checkout(
        @Body request: CheckoutRequest
    ): Map<String, Any>

    @GET("cart/orders")
    suspend fun getOrders(): List<OrderItem>
}

object RetrofitInstance {
    private var currentBaseUrl = SessionManager.getBaseUrl()
    private var retrofitApi: ApiService? = null

    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val builder = original.newBuilder()

        val token = SessionManager.getToken()
        if (!token.isNullOrEmpty()) {
            builder.addHeader("Authorization", "Bearer $token")
            builder.addHeader("Cookie", "token=$token")
        }

        builder.addHeader("Accept", "application/json")
        chain.proceed(builder.build())
    }

    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }

    val api: ApiService
        get() {
            val configuredUrl = SessionManager.getBaseUrl()
            if (retrofitApi == null || currentBaseUrl != configuredUrl) {
                currentBaseUrl = configuredUrl
                retrofitApi = Retrofit.Builder()
                    .baseUrl(currentBaseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService::class.java)
            }
            return retrofitApi!!
        }

    fun getProductImageUrl(filename: String): String {
        val cleanName = if (filename.endsWith(".jpg") || filename.endsWith(".png")) filename else "$filename.jpg"
        return "${SessionManager.getBaseUrl()}img_products/$cleanName"
    }
}
