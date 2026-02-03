package com.example.adidusshop.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fitInside
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.adidusshop.viewModels.CurrencyUtil
import com.example.adidusshop.viewModels.ProductState
import com.example.adidusshop.viewModels.ProductViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    navController: NavController,
    product_id: Int,
    viewModel: ProductViewModel = viewModel()
) {
    LaunchedEffect(product_id) {
        viewModel.getProductId(product_id)

    }
    LaunchedEffect(Unit) {
        viewModel.getAllProduct()
    }



    when (val state = viewModel.productDetailState  ) {
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
            val rating = remember { (Random.nextInt(3, 11)) / 2.0 }
            val review = remember { (1..200).random() }
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("ADIDUS", fontWeight = FontWeight.Bold) },
                        navigationIcon = {
                            IconButton(
                                onClick = { navController.popBackStack() }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBackIosNew,
                                    contentDescription = "IconArrowBack"
                                )
                            }
                        },
                        actions = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = "IconSearch",
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(20.dp))
                            Icon(
                                Icons.Outlined.Notifications,
                                contentDescription = "IconNotification",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    )
                },
                bottomBar = {
                    BottomAppBar{
                        BarOrder()
                    }

                }

            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    AsyncImage(
                        model = "http://10.0.2.2:3000/img_products/${product.image_filename}.jpg",
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    when (val listState = viewModel.productListState) {

                        is ProductState.Success -> {
                            LazyRow(
                                modifier = Modifier
                                    .padding(horizontal = 12.dp),
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(listState.products) { product ->
                                    Column(
                                        modifier = Modifier
                                            .border(1.dp, color = Color.Black)
                                            .width(120.dp)

                                            .clickable {
                                                navController.navigate("detail/${product.product_id}")
                                            }
                                    ) {
                                        AsyncImage(
                                            model = "http://10.0.2.2:3000/img_products/${product.image_filename}.jpg",
                                            contentDescription = product.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f)
                                        )

                                        Spacer(modifier = Modifier.height(6.dp))

                                    }
                                }
                            }
                        }

                        is ProductState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(16.dp)
                            )
                        }

                        else -> {}
                    }

                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(product.name, fontWeight = FontWeight.Bold, fontSize = 30.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row {
                            repeat(5) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "IconStar",
                                    tint = Color(0xffFFD41D),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text("$rating", fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.width(16.dp))

                                Text("( $review Reviews )", fontWeight = FontWeight.Bold)

                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = CurrencyUtil.baht(product.price),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp

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

@Composable
fun BarOrder(){
    var setPopUpSuccess by remember { mutableStateOf(false) }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {},
            modifier = Modifier

                .weight(1f).height(50.dp), shape = RoundedCornerShape(8.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White)
        ) {
            Text("Cart" , fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }
Spacer(modifier = Modifier.width(10.dp))
        Button(
            onClick = {
                setPopUpSuccess = true
            },
            modifier = Modifier.weight(1f).height(50.dp), shape =RoundedCornerShape(8.dp)
            ,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Green,
                contentColor = Color.White)

        ) {
            Text("Buy" , fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }


        if(setPopUpSuccess){
            AlertDialog(
                onDismissRequest = {setPopUpSuccess = false},
                title = {Text("Order Success", fontWeight = FontWeight.Bold)},
                text = {
                    Text("Your order has been placed successfully")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            setPopUpSuccess = false
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Green,
                            contentColor = Color.White
                        )
                    ) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Cancel")
                    }
                },
            )
        }
    }
}
