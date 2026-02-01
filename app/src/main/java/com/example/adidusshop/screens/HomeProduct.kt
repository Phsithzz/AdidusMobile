package com.example.adidusshop.screens


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adidusshop.viewModels.ProductState
import com.example.adidusshop.viewModels.ProductViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeProduct(
    viewModel: ProductViewModel = viewModel()
) {
    val tab = listOf("All","Sneaker", "Football", "Basketball", "FlipFlops")
    var selectedTab by remember { mutableStateOf(0) }

    LaunchedEffect(selectedTab) {
        if (selectedTab == 0) {
            viewModel.getAllProduct()
        } else {
            viewModel.getProductType(tab[selectedTab])
        }
    }
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text("Adidus", fontWeight = FontWeight.Bold) },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "cart Icon")
                        }
                    }

                )
                TabRow(
                    selectedTabIndex = selectedTab
                ) {
                tab.forEachIndexed { index,t->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {selectedTab = index},
                        text = {Text(t, fontSize = 10.sp)}
                    )
                }
            }

            }
        }
    ) {padding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewModel.productState) {
                is ProductState.Loading -> {
                    CircularProgressIndicator()
                }

                is ProductState.Success -> {
                    LazyColumn {
                        items(state.products) { product ->
                            Column() {
                                AsyncImage(
                                    model = "http://10.0.2.2:3000/img_products/${product.image_filename}.jpg",
                                    contentDescription = product.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp),
                                    contentScale = ContentScale.Crop
                                )
                                Text(
                                    text = "${product.brand} - ${product.name}",
                                    modifier = Modifier.padding(16.dp)
                                )
                            }

                        }
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


    }

}
