package com.example.adidusshop.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.adidusshop.models.requestModel.AddressRequest
import com.example.adidusshop.viewModels.CartViewModel
import com.example.adidusshop.viewModels.CheckoutState
import com.example.adidusshop.viewModels.CurrencyUtil
import com.example.adidusshop.viewModels.OrderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    navController: NavController,
    cartViewModel: CartViewModel,
    orderViewModel: OrderViewModel
) {
    val context = LocalContext.current
    val checkoutState by orderViewModel.checkoutState.collectAsState()

    var houseNumber by remember { mutableStateOf("123/45") }
    var villageNumber by remember { mutableStateOf("5") }
    var subdistrict by remember { mutableStateOf("พญาไท") }
    var district by remember { mutableStateOf("พญาไท") }
    var province by remember { mutableStateOf("กรุงเทพมหานคร") }
    var postalCode by remember { mutableStateOf("10400") }

    val paymentOptions = listOf("เก็บเงินปลายทาง (Cash on Delivery)", "โอนผ่าน PromptPay", "บัตรเครดิต / เดบิต")
    var selectedPayment by remember { mutableStateOf(paymentOptions[0]) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var placedOrderId by remember { mutableStateOf(0) }

    val subtotal = cartViewModel.subtotalPrice
    val shippingFee = 0
    val totalAmount = subtotal + shippingFee

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ยืนยันการสั่งซื้อ", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 8.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("ยอดชำระสุทธิ", fontSize = 16.sp, color = Color.Gray)
                        Text(
                            CurrencyUtil.baht(totalAmount),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = {
                            if (houseNumber.isBlank() || subdistrict.isBlank() || district.isBlank() || province.isBlank() || postalCode.isBlank()) {
                                Toast.makeText(context, "กรุณากรอกข้อมูลที่อยู่ให้ครบถ้วน", Toast.LENGTH_SHORT).show()
                                return@Button
                            }

                            orderViewModel.checkout(
                                address = AddressRequest(
                                    house_number = houseNumber,
                                    village_number = villageNumber,
                                    subdistrict = subdistrict,
                                    district = district,
                                    province = province,
                                    postal_code = postalCode
                                ),
                                paymentMethod = selectedPayment,
                                onSuccess = { id ->
                                    cartViewModel.clearCart()
                                    placedOrderId = id
                                    showSuccessDialog = true
                                },
                                onError = { err ->
                                    Toast.makeText(context, err, Toast.LENGTH_SHORT).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                        enabled = checkoutState !is CheckoutState.Loading && cartViewModel.cartItems.isNotEmpty()
                    ) {
                        if (checkoutState is CheckoutState.Loading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("สั่งซื้อสินค้าทันที", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF9FAFB))
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Shipping Address Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ที่อยู่สำหรับจัดส่งสินค้า", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = houseNumber,
                            onValueChange = { houseNumber = it },
                            label = { Text("บ้านเลขที่ / อาคาร") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = villageNumber,
                            onValueChange = { villageNumber = it },
                            label = { Text("หมู่ที่") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = subdistrict,
                            onValueChange = { subdistrict = it },
                            label = { Text("ตำบล / แขวง") },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = district,
                            onValueChange = { district = it },
                            label = { Text("อำเภอ / เขต") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = province,
                            onValueChange = { province = it },
                            label = { Text("จังหวัด") },
                            modifier = Modifier.weight(1.2f)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = { Text("รหัสไปรษณีย์") },
                            modifier = Modifier.weight(0.8f)
                        )
                    }
                }
            }

            // Payment Method Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Payment, contentDescription = null, tint = Color.Black)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ช่องทางการชำระเงิน", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))

                    paymentOptions.forEach { option ->
                        val isSelected = selectedPayment == option
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedPayment = option }
                                .padding(vertical = 4.dp)
                        ) {
                            RadioButton(
                                selected = isSelected,
                                onClick = { selectedPayment = option },
                                colors = RadioButtonDefaults.colors(selectedColor = Color.Black)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            val icon = when (option) {
                                paymentOptions[0] -> Icons.Default.LocalShipping
                                paymentOptions[1] -> Icons.Default.QrCode
                                else -> Icons.Default.CreditCard
                            }
                            Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.DarkGray)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(option, fontSize = 14.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }
            }

            // Order Items Summary
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("สรุปคำสั่งซื้อ (${cartViewModel.itemCount} ชิ้น)", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    cartViewModel.cartItems.forEach { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                "${item.name} (${item.size}) x${item.quantity}",
                                fontSize = 14.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                CurrencyUtil.baht(item.price * item.quantity),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = Color(0xFFEEEEEE))
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("ค่าจัดส่ง", fontSize = 14.sp, color = Color.Gray)
                        Text("ฟรี", fontSize = 14.sp, color = Color(0xFF10B981), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            icon = {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF10B981),
                    modifier = Modifier.size(56.dp)
                )
            },
            title = {
                Text(
                    "สั่งซื้อสำเร็จ!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Text(
                    "ระบบได้รับคำสั่งซื้อหมายเลข #$placedOrderId เรียบร้อยแล้ว ทีมงานกำลังเร่งจัดส่งสินค้าให้ถึงมือคุณอย่างรวดเร็วที่สุด",
                    fontSize = 14.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        orderViewModel.resetCheckoutState()
                        navController.navigate("orders") {
                            popUpTo("home") { inclusive = false }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("ดูประวัติคำสั่งซื้อ")
                }
            }
        )
    }
}
