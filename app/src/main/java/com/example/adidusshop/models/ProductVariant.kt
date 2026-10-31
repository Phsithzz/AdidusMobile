package com.example.adidusshop.models

data class ProductVariant(
    val variant_id: Int,
    val product_id: Int? = null,
    val stock_quantity: Int = 0,
    val price: Int = 0,
    val size: String = "",
    val color: String = ""
)
