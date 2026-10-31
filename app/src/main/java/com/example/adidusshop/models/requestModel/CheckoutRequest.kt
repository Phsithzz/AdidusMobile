package com.example.adidusshop.models.requestModel

data class AddressRequest(
    val house_number: String,
    val village_number: String,
    val subdistrict: String,
    val district: String,
    val province: String,
    val postal_code: String
)

data class CheckoutRequest(
    val address: AddressRequest,
    val payment_method: String
)
