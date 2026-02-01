package com.example.adidusshop.viewModels

import com.example.adidusshop.models.Product

sealed class ProductState {
    object Idle: ProductState()
    object Loading: ProductState()
    data class Success(val products: List<Product>): ProductState()
    data class Error( val message: String): ProductState()
}