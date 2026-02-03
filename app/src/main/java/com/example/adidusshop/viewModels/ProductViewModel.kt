package com.example.adidusshop.viewModels

import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.adidusshop.services.RetrofitInstance
import kotlinx.coroutines.launch

class ProductViewModel: ViewModel(){


    var productListState by mutableStateOf<ProductState>(ProductState.Idle)
        private set
    var productDetailState by mutableStateOf<ProductState>(ProductState.Idle)
        private set

    fun getAllProduct() {
        productListState = ProductState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getProduct()
                productListState = ProductState.Success(result)
            } catch (e: Exception) {
                productListState = ProductState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun getProductType(description: String) {
        productDetailState = ProductState.Loading

        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.getProductType(description)
                productDetailState = ProductState.Success(result)
            } catch (e: Exception) {
                productDetailState = ProductState.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun getProductId(product_id:Int){
        productDetailState = ProductState.Loading

        viewModelScope.launch {
            try {
                val product = RetrofitInstance.api.getProductId(product_id)
                productDetailState = ProductState.DetailSuccess(product)
            }
            catch (e: Exception){
                productDetailState = ProductState.Error(e.message ?:"Unknown error")
            }
        }
    }
}