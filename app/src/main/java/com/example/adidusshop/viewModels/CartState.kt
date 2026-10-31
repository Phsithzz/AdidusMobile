package com.example.adidusshop.viewModels

import com.example.adidusshop.models.CartItem

sealed class CartState {
    object Idle : CartState()
    object Loading : CartState()
    data class Success(val items: List<CartItem>) : CartState()
    data class Error(val message: String) : CartState()
}
