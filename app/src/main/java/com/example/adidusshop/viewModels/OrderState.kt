package com.example.adidusshop.viewModels

import com.example.adidusshop.models.OrderGroup

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    data class Success(val orders: List<OrderGroup>) : OrderState()
    data class Error(val message: String) : OrderState()
}

sealed class CheckoutState {
    object Idle : CheckoutState()
    object Loading : CheckoutState()
    data class Success(val orderId: Int, val message: String) : CheckoutState()
    data class Error(val message: String) : CheckoutState()
}
