package com.example.adidusshop.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adidusshop.models.OrderGroup
import com.example.adidusshop.models.OrderItem
import com.example.adidusshop.models.requestModel.AddressRequest
import com.example.adidusshop.models.requestModel.CheckoutRequest
import com.example.adidusshop.services.MockData
import com.example.adidusshop.services.RetrofitInstance
import com.example.adidusshop.services.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OrderViewModel : ViewModel() {

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState

    private val _checkoutState = MutableStateFlow<CheckoutState>(CheckoutState.Idle)
    val checkoutState: StateFlow<CheckoutState> = _checkoutState

    var orderList by mutableStateOf<List<OrderGroup>>(emptyList())
        private set

    init {
        loadOrders()
    }

    fun loadOrders() {
        viewModelScope.launch {
            _orderState.value = OrderState.Loading

            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                orderList = MockData.mockOrders
                _orderState.value = OrderState.Success(orderList)
                return@launch
            }

            try {
                val rawItems = RetrofitInstance.api.getOrders()
                if (rawItems.isEmpty()) {
                    orderList = MockData.mockOrders
                    _orderState.value = OrderState.Success(orderList)
                } else {
                    val grouped = rawItems.groupBy { it.order_id }.map { (orderId, items) ->
                        val first = items.first()
                        val addr = listOfNotNull(
                            first.house_number?.let { "เลขที่ $it" },
                            first.village_number?.let { "หมู่ $it" },
                            first.subdistrict?.let { "ต.$it" },
                            first.district?.let { "อ.$it" },
                            first.province?.let { "จ.$it" },
                            first.postal_code
                        ).joinToString(" ")

                        OrderGroup(
                            order_id = orderId,
                            total_price = first.total_price,
                            payment_method = first.payment_method,
                            address = addr.ifBlank { "จัดส่งตามที่อยู่ที่ระบุไว้" },
                            items = items
                        )
                    }
                    orderList = grouped
                    _orderState.value = OrderState.Success(grouped)
                }
            } catch (e: Exception) {
                orderList = MockData.mockOrders
                _orderState.value = OrderState.Success(orderList)
            }
        }
    }

    fun checkout(
        address: AddressRequest,
        paymentMethod: String,
        onSuccess: (orderId: Int) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            _checkoutState.value = CheckoutState.Loading

            if (SessionManager.isMockMode() || !SessionManager.isLoggedIn()) {
                val newOrderId = (1002..9999).random()
                val addrStr = "${address.house_number} ต.${address.subdistrict} อ.${address.district} จ.${address.province} ${address.postal_code}"
                val newOrder = OrderGroup(
                    order_id = newOrderId,
                    total_price = MockData.mockCartItems.sumOf { it.price * it.quantity }.toDouble(),
                    payment_method = paymentMethod,
                    address = addrStr,
                    items = MockData.mockCartItems.map {
                        OrderItem(
                            cart_id = it.cart_id,
                            quantity = it.quantity,
                            price = it.price,
                            product_id = it.product_id,
                            name = it.name,
                            description = it.description,
                            image_filename = it.image_filename,
                            size = it.size,
                            order_id = newOrderId,
                            total_price = (it.price * it.quantity).toDouble(),
                            payment_method = paymentMethod
                        )
                    }
                )
                MockData.mockOrders.add(0, newOrder)
                MockData.mockCartItems.clear()
                orderList = MockData.mockOrders.toList()
                _checkoutState.value = CheckoutState.Success(newOrderId, "สั่งซื้อสำเร็จ")
                onSuccess(newOrderId)
                return@launch
            }

            try {
                val req = CheckoutRequest(address = address, payment_method = paymentMethod)
                val res = RetrofitInstance.api.checkout(req)
                val orderId = (res["order_id"] as? Number)?.toInt() ?: (1000..9999).random()
                loadOrders()
                _checkoutState.value = CheckoutState.Success(orderId, "สั่งซื้อสำเร็จ")
                onSuccess(orderId)
            } catch (e: Exception) {
                // Fallback local checkout
                val newOrderId = (1002..9999).random()
                val addrStr = "${address.house_number} ต.${address.subdistrict} อ.${address.district} จ.${address.province} ${address.postal_code}"
                val newOrder = OrderGroup(
                    order_id = newOrderId,
                    total_price = 3800.0,
                    payment_method = paymentMethod,
                    address = addrStr,
                    items = emptyList()
                )
                MockData.mockOrders.add(0, newOrder)
                orderList = MockData.mockOrders.toList()
                _checkoutState.value = CheckoutState.Success(newOrderId, "สั่งซื้อสำเร็จ")
                onSuccess(newOrderId)
            }
        }
    }

    fun resetCheckoutState() {
        _checkoutState.value = CheckoutState.Idle
    }
}
