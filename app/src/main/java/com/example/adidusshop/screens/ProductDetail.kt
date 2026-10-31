package com.example.adidusshop.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.adidusshop.models.Product
import com.example.adidusshop.models.ProductVariant
import com.example.adidusshop.services.RetrofitInstance
import com.example.adidusshop.viewModels.CartViewModel
import com.example.adidusshop.viewModels.CurrencyUtil
import com.example.adidusshop.viewModels.ProductState
import com.example.adidusshop.viewModels.ProductViewModel
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetail(
    navController: NavController,
    product_id: Int,
    viewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(product_id) {
        viewModel.getProductId(product_id)
        viewModel.getAllProduct()
    }

    when (val state = viewModel.productDetailState) {
        is ProductState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.Black)
            }
        }

        is ProductState.DetailSuccess -> {
            val product = state.products
            val rating = remember { (Random.nextInt(8, 11)) / 2.0 }
            val review = remember { (24..180).random() }

            // Extract variants or fallback sizes
            val variants = product.variants?.ifEmpty { null } ?: listOf(
                ProductVariant(1, product.product_id, 10, product.price, "7.5 UK", "Standard"),
                ProductVariant(2, product.product_id, 15, product.price, "8.0 UK", "Standard"),
                ProductVariant(3, product.product_id, 12, product.price, "8.5 UK", "Standard"),
                ProductVariant(4, product.product_id, 20, product.price, "9.0 UK", "Standard"),
                ProductVariant(5, product.product_id, 14, product.price, "9.5 UK", "Standard"),
                ProductVariant(6, product.product_id, 8, product.price, "10.0 UK", "Standard")
            )

            var selectedVariant by remember { mutableStateOf(variants.first()) }
            var quantity by remember { mutableIntStateOf(1) }

            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text("ADIDUS", fontWeight = FontWeight.Bold, letterSpacing = 2.sp) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                            }
                        },
                        actions = {
                            IconButton(onClick = { navController.navigate("cart") }) {
                                BadgedBox(
                                    badge = {
                                        if (cartViewModel.itemCount > 0) {
                                            Badge(containerColor = Color.Red, contentColor = Color.White) {
                                                Text("${cartViewModel.itemCount}")
                                            }
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                                }
                            }
                        },
                        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
                    )
                },
                bottomBar = {
                    Surface(
                        shadowElevation = 10.dp,
                        color = Color.White
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Button(
                                onClick = {
                                    cartViewModel.addToCart(
                                        variantId = selectedVariant.variant_id,
                                        quantity = quantity,
                                        onSuccess = {
                                            Toast.makeText(
                                                context,
                                                "เพิ่ม ${product.name} (${selectedVariant.size}) ลงตะกร้าแล้ว",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        },
                                        onError = {
                                            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFF3F4F6),
                                    contentColor = Color.Black
                                )
                            ) {
                                Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("ใส่ตะกร้า", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }

                            Button(
                                onClick = {
                                    cartViewModel.addToCart(
                                        variantId = selectedVariant.variant_id,
                                        quantity = quantity,
                                        onSuccess = {
                                            navController.navigate("cart")
                                        },
                                        onError = {
                                            navController.navigate("cart")
                                        }
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .height(50.dp),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("ซื้อทันที", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            }
                        }
                    }
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Main Product Image
                    AsyncImage(
                        model = RetrofitInstance.getProductImageUrl(product.image_filename),
                        contentDescription = product.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.1f)
                            .background(Color(0xFFF9FAFB))
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Related / Other Products Carousel
                    when (val listState = viewModel.productListState) {
                        is ProductState.Success -> {
                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                contentPadding = PaddingValues(horizontal = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(listState.products.take(6)) { other ->
                                    val isCurrent = other.product_id == product.product_id
                                    Box(
                                        modifier = Modifier
                                            .size(72.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .border(
                                                width = if (isCurrent) 2.dp else 1.dp,
                                                color = if (isCurrent) Color.Black else Color(0xFFE5E7EB),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                navController.navigate("detail/${other.product_id}")
                                            }
                                    ) {
                                        AsyncImage(
                                            model = RetrofitInstance.getProductImageUrl(other.image_filename),
                                            contentDescription = other.name,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }
                                }
                            }
                        }
                        else -> {}
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Product Details Content
                    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    product.brand.ifBlank { "ADIDAS" }.uppercase(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    product.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    lineHeight = 28.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Rating & Reviews
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(5) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = "Star",
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "$rating",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                "($review รีวิว)",
                                fontSize = 13.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Price
                        Text(
                            text = CurrencyUtil.baht(product.price),
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFF3F4F6))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Size Variant Selector
                        Text(
                            "เลือกขนาดไซส์ (Size):",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 4.dp)
                        ) {
                            items(variants) { variant ->
                                val isSelected = selectedVariant.variant_id == variant.variant_id
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) Color.Black else Color.White,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) Color.Black else Color(0xFFD1D5DB)
                                    ),
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable { selectedVariant = variant }
                                ) {
                                    Text(
                                        text = variant.size,
                                        color = if (isSelected) Color.White else Color.Black,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Quantity Selector & Stock Status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("จำนวน:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "คงเหลือ ${selectedVariant.stock_quantity.coerceAtLeast(5)} ชิ้น",
                                    fontSize = 12.sp,
                                    color = Color(0xFF10B981)
                                )
                            }

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(Color(0xFFF3F4F6), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                IconButton(
                                    onClick = { if (quantity > 1) quantity-- },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Remove, contentDescription = "Decrease", modifier = Modifier.size(16.dp))
                                }

                                Text(
                                    "$quantity",
                                    modifier = Modifier.padding(horizontal = 12.dp),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )

                                IconButton(
                                    onClick = { if (quantity < selectedVariant.stock_quantity.coerceAtLeast(10)) quantity++ },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "Increase", modifier = Modifier.size(16.dp))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = Color(0xFFF3F4F6))
                        Spacer(modifier = Modifier.height(16.dp))

                        // Description / Story
                        Text(
                            "รายละเอียดสินค้า",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (!product.detail.isNullOrBlank()) product.detail else "สัมผัสความสบายและสไตล์อันเป็นเอกลักษณ์จาก Adidus ด้วยโครงสร้างที่ออกแบบตามหลักสรีรศาสตร์ วัสดุคุณภาพสูงทนทาน ระบายอากาศได้ดีเยี่ยม เหมาะสำหรับทุกกิจกรรมและการสวมใส่ในชีวิตประจำวัน",
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            color = Color(0xFF4B5563)
                        )

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }

        is ProductState.Error -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = state.message, color = Color.Red)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(onClick = { viewModel.getProductId(product_id) }) {
                        Text("ลองใหม่อีกครั้ง")
                    }
                }
            }
        }

        else -> {}
    }
}
