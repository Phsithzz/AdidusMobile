package com.example.adidusshop.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adidusshop.models.CartItem
import com.example.adidusshop.models.requestModel.AddCartRequest
import com.example.adidusshop.models.requestModel.UpdateCartQuantityRequest
import com.example.adidusshop.services.MockData
import com.example.adidusshop.services.RetrofitInstance
import com.example.adidusshop.services.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _cartState = MutableStateFlow<CartState>(CartState.Idle)
    val cartState: StateFlow<CartState> = _cartState

    var cartItems by mutableStateOf<List<CartItem>>(emptyList())
        private set

    val itemCount: Int
        get() = cartItems.sumOf { it.quantity }

    val subtotalPrice: Int
        get() = cartItems.sumOf { it.price * it.quantity }

    init {
        loadCart()
    }

    fun loadCart() {
        viewModelScope.launch {
            _cartState.value = CartState.Loading

            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                cartItems = MockData.mockCartItems
                _cartState.value = CartState.Success(cartItems)
                return@launch
            }

            try {
                val items = RetrofitInstance.api.getCart()
                cartItems = items
                _cartState.value = CartState.Success(items)
            } catch (e: Exception) {
                // Fallback to mock cart if network error
                cartItems = MockData.mockCartItems
                _cartState.value = CartState.Success(cartItems)
            }
        }
    }

    fun addToCart(
        variantId: Int,
        quantity: Int,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                // Mock add to cart
                val existing = MockData.mockCartItems.find { it.cart_id == variantId }
                if (existing != null) {
                    val idx = MockData.mockCartItems.indexOf(existing)
                    MockData.mockCartItems[idx] = existing.copy(quantity = existing.quantity + quantity)
                } else {
                    MockData.mockCartItems.add(
                        CartItem(
                            cart_id = (10..999).random(),
                            quantity = quantity,
                            price = 3800,
                            product_id = 1,
                            name = "Adidas Shoes",
                            description = "Sneaker",
                            image_filename = "lifestyle1",
                            size = "9.0 UK",
                            stock_quantity = 10
                        )
                    )
                }
                cartItems = MockData.mockCartItems.toList()
                _cartState.value = CartState.Success(cartItems)
                onSuccess()
                return@launch
            }

            try {
                val res = RetrofitInstance.api.addCart(AddCartRequest(variantId, quantity))
                if (res.cartOK) {
                    loadCart()
                    onSuccess()
                } else {
                    onError(res.message ?: "ไม่สามารถเพิ่มลงตะกร้าได้")
                }
            } catch (e: Exception) {
                // Fallback to local
                MockData.mockCartItems.add(
                    CartItem(
                        cart_id = (10..999).random(),
                        quantity = quantity,
                        price = 3800,
                        product_id = 1,
                        name = "Adidas Sneaker",
                        description = "Sneaker",
                        image_filename = "lifestyle1",
                        size = "9.0 UK",
                        stock_quantity = 10
                    )
                )
                cartItems = MockData.mockCartItems.toList()
                _cartState.value = CartState.Success(cartItems)
                onSuccess()
            }
        }
    }

    fun updateQuantity(cartId: Int, newQuantity: Int) {
        if (newQuantity <= 0) {
            removeItem(cartId)
            return
        }

        viewModelScope.launch {
            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                val idx = MockData.mockCartItems.indexOfFirst { it.cart_id == cartId }
                if (idx != -1) {
                    MockData.mockCartItems[idx] = MockData.mockCartItems[idx].copy(quantity = newQuantity)
                    cartItems = MockData.mockCartItems.toList()
                    _cartState.value = CartState.Success(cartItems)
                }
                return@launch
            }

            try {
                RetrofitInstance.api.updateCartQuantity(cartId, UpdateCartQuantityRequest(newQuantity))
                loadCart()
            } catch (e: Exception) {
                // local update
                cartItems = cartItems.map {
                    if (it.cart_id == cartId) it.copy(quantity = newQuantity) else it
                }
            }
        }
    }

    fun removeItem(cartId: Int) {
        viewModelScope.launch {
            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                MockData.mockCartItems.removeAll { it.cart_id == cartId }
                cartItems = MockData.mockCartItems.toList()
                _cartState.value = CartState.Success(cartItems)
                return@launch
            }

            try {
                RetrofitInstance.api.removeCartItem(cartId)
                loadCart()
            } catch (e: Exception) {
                cartItems = cartItems.filter { it.cart_id != cartId }
            }
        }
    }

    fun clearCart() {
        cartItems = emptyList()
        MockData.mockCartItems.clear()
        _cartState.value = CartState.Success(emptyList())
    }
}
