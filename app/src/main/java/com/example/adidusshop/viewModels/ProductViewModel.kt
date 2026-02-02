package com.example.adidusshop.viewModels

import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.adidusshop.services.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel(){

    var productState by mutableStateOf<ProductState>(ProductState.Idle)
        private set


    fun getAllProduct() {
        productState = ProductState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getProduct()
                productState = ProductState.Success(result)
            } catch (e: Exception) {
                productState = ProductState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun getProductType(description: String) {
        productState = ProductState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getProductType(description)
                productState = ProductState.Success(result)
            } catch (e: Exception) {
                productState = ProductState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getProductId(product_id:Int){
        productState = ProductState.Loading

        viewModelScope.launch {
            try {
                val product = RetrofitInstance.api.getProductId(product_id)
                productState = ProductState.DetailSuccess(product)
            }
            catch (e: Exception){
                productState = ProductState.Error(e.message ?:"Unknown error")
            }
        }
    }
}