package com.example.adidusshop.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.adidusshop.models.Product
import com.example.adidusshop.services.MockData
import com.example.adidusshop.services.RetrofitInstance
import com.example.adidusshop.services.SessionManager
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    var productListState by mutableStateOf<ProductState>(ProductState.Idle)
        private set
    var productDetailState by mutableStateOf<ProductState>(ProductState.Idle)
        private set

    fun getAllProduct() {
        productListState = ProductState.Loading

        viewModelScope.launch {
            if (SessionManager.isMockMode()) {
                productListState = ProductState.Success(MockData.sampleProducts)
                return@launch
            }

            try {
                val result = RetrofitInstance.api.getProduct()
                if (result.isNotEmpty()) {
                    productListState = ProductState.Success(result)
                } else {
                    productListState = ProductState.Success(MockData.sampleProducts)
                }
            } catch (e: Exception) {
                // Fallback to sample data for smooth presentation
                productListState = ProductState.Success(MockData.sampleProducts)
            }
        }
    }

    fun getProductType(description: String) {
        productListState = ProductState.Loading

        viewModelScope.launch {
            if (SessionManager.isMockMode()) {
                val filtered = MockData.sampleProducts.filter {
                    it.description.equals(description, ignoreCase = true) ||
                    it.category_name?.equals(description, ignoreCase = true) == true
                }
                productListState = ProductState.Success(filtered.ifEmpty { MockData.sampleProducts })
                return@launch
            }

            try {
                val result = RetrofitInstance.api.getProductType(description)
                if (result.isNotEmpty()) {
                    productListState = ProductState.Success(result)
                } else {
                    val filtered = MockData.sampleProducts.filter {
                        it.description.equals(description, ignoreCase = true) ||
                        it.category_name?.equals(description, ignoreCase = true) == true
                    }
                    productListState = ProductState.Success(filtered.ifEmpty { MockData.sampleProducts })
                }
            } catch (e: Exception) {
                val filtered = MockData.sampleProducts.filter {
                    it.description.equals(description, ignoreCase = true) ||
                    it.category_name?.equals(description, ignoreCase = true) == true
                }
                productListState = ProductState.Success(filtered.ifEmpty { MockData.sampleProducts })
            }
        }
    }

    fun searchProduct(query: String) {
        if (query.isBlank()) {
            getAllProduct()
            return
        }

        productListState = ProductState.Loading
        viewModelScope.launch {
            try {
                val result = RetrofitInstance.api.searchProducts(query)
                if (result.isNotEmpty()) {
                    productListState = ProductState.Success(result)
                } else {
                    val filtered = MockData.sampleProducts.filter {
                        it.name.contains(query, ignoreCase = true) ||
                        it.brand.contains(query, ignoreCase = true) ||
                        it.description.contains(query, ignoreCase = true)
                    }
                    productListState = ProductState.Success(filtered)
                }
            } catch (e: Exception) {
                val filtered = MockData.sampleProducts.filter {
                    it.name.contains(query, ignoreCase = true) ||
                    it.brand.contains(query, ignoreCase = true) ||
                    it.description.contains(query, ignoreCase = true)
                }
                productListState = ProductState.Success(filtered)
            }
        }
    }

    fun getProductId(product_id: Int) {
        productDetailState = ProductState.Loading

        viewModelScope.launch {
            if (SessionManager.isMockMode()) {
                val found = MockData.sampleProducts.find { it.product_id == product_id }
                    ?: MockData.sampleProducts.first()
                productDetailState = ProductState.DetailSuccess(found)
                return@launch
            }

            try {
                val product = RetrofitInstance.api.getProductId(product_id)
                productDetailState = ProductState.DetailSuccess(product)
            } catch (e: Exception) {
                val found = MockData.sampleProducts.find { it.product_id == product_id }
                    ?: MockData.sampleProducts.first()
                productDetailState = ProductState.DetailSuccess(found)
            }
        }
    }
}
