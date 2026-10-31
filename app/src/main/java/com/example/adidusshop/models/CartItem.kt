package com.example.adidusshop.models

data class CartItem(
    val cart_id: Int,
    val quantity: Int,
    val price: Int,
    val product_id: Int,
    val name: String,
    val description: String,
    val image_filename: String,
    val size: String,
    val stock_quantity: Int
)
