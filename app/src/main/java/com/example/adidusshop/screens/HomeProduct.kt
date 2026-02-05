package com.example.adidusshop.screens


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adidusshop.viewModels.ProductState
import com.example.adidusshop.viewModels.ProductViewModel
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.adidusshop.viewModels.CurrencyUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeProduct(
    navController: NavController,
    viewModel: ProductViewModel = viewModel()
) {
    val tab = listOf("All", "Sneaker", "Football", "FlipFlops")
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
                    title = {
                        Text(
                            "Adidus",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    },
                    actions = {
                        IconButton(
                            onClick = {}
                        ) {
                            Icon(Icons.Default.ShoppingCart, contentDescription = "cart Icon")
                        }
                    }

                )
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color.Red,
                            height = 2.dp
                        )
                    }
                ) {
                    tab.forEachIndexed { index, t ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = { Text(t, fontSize = 14.sp) }
                        )
                    }
                }

            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center,

        ) {
            when (val state = viewModel.productListState) {
                is ProductState.Loading -> {
                    CircularProgressIndicator()
                }

                is ProductState.Success -> {

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(state.products) { index, product ->
                            Card(
                                onClick = {
                                    navController.navigate("detail/${product.product_id}")
                                },
                                elevation = CardDefaults.cardElevation(10.dp),
                                colors = CardDefaults.cardColors(Color.White),

                                ) {
                                var likeToggle by remember { mutableStateOf(false) }
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp)
                                        .height(350.dp),
                                    verticalArrangement = Arrangement.SpaceBetween,
                                    horizontalAlignment = Alignment.CenterHorizontally,

                                    ) {
                                    Column {
                                        AsyncImage(
                                            model = "http://10.0.2.2:3000/img_products/${product.image_filename}.jpg",
                                            contentDescription = product.name,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(modifier = Modifier.height(10.dp))
                                        Text(
                                            text = product.brand,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = product.name,
                                            fontSize = 14.sp,
                                            modifier = Modifier.alpha(0.6f)
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Text(
                                            text = CurrencyUtil.baht(product.price),
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        IconButton(
                                            onClick = { likeToggle = !likeToggle },
                                            modifier = Modifier.size(30.dp)
                                        ) {
                                            Icon(
                                                imageVector = if (likeToggle) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                                                contentDescription = "FavIcon",
                                                tint = if (likeToggle) Color.Red else Color.Black
                                            )
                                        }


                                    }


                                }
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
