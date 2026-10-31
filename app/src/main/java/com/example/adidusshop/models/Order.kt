package com.example.adidusshop.models

data class OrderItem(
    val cart_id: Int,
    val quantity: Int,
    val price: Int,
    val product_id: Int,
    val name: String,
    val description: String,
    val image_filename: String,
    val size: String,
    val order_id: Int,
    val total_price: Double,
    val payment_method: String,
    val house_number: String? = null,
    val village_number: String? = null,
    val subdistrict: String? = null,
    val district: String? = null,
    val province: String? = null,
    val postal_code: String? = null
)

data class OrderGroup(
    val order_id: Int,
    val total_price: Double,
    val payment_method: String,
    val address: String,
    val items: List<OrderItem>
)
