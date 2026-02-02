package com.example.adidusshop.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.adidusshop.viewModels.ProductState
import com.example.adidusshop.viewModels.ProductViewModel

@Composable
fun ProductDetail(
    product_id: Int,
    viewModel: ProductViewModel = viewModel()
) {
    LaunchedEffect(product_id) {
        viewModel.getProductId(product_id)
    }

    when (val state = viewModel.productState) {
        is ProductState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is ProductState.DetailSuccess -> {
            val product = state.products
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                AsyncImage(
                    model = "http://10.0.2.2:3000/img_products/${product.image_filename}.jpg",
                    contentDescription = product.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxWidth().aspectRatio(1f)
                )
                Text(product.name)
            }
        }

        is ProductState.Error -> {
            Text(
                text = state.message,
                color = Color.Red
            )
        }

        else -> {}

    }


}
