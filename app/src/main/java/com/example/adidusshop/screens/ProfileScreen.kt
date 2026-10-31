package com.example.adidusshop.screens

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.example.adidusshop.services.SessionManager
import com.example.adidusshop.viewModels.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    var isMockMode by remember { mutableStateOf(SessionManager.isMockMode()) }
    var currentBaseUrl by remember { mutableStateOf(SessionManager.getBaseUrl()) }
    var showEditUrlDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val name = SessionManager.getUserName().ifBlank { "ผู้ใช้งาน Adidus" }
    val lastname = SessionManager.getUserLastname()
    val email = SessionManager.getUserEmail().ifBlank { "customer@adidus.com" }
    val role = SessionManager.getUserRole()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("โปรไฟล์ & ตั้งค่า", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
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
            // Profile Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.Black),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = name.firstOrNull()?.uppercase() ?: "A",
                            color = Color.White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            "$name $lastname".trim(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            email,
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Surface(
                            shape = RoundedCornerShape(4.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            Text(
                                if (role == "admin") "ผู้ดูแลระบบ (Admin)" else "สมาชิกทั่วไป (Customer)",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }

            // Connection & Server Settings
            Text("การเชื่อมต่อเซิร์ฟเวอร์", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Dns, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Backend API Host", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text(currentBaseUrl, fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        IconButton(onClick = { showEditUrlDialog = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Host", tint = Color.Gray)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFFF3F4F6))
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Science, contentDescription = null, tint = Color(0xFF8B5CF6))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("โหมดจำลองข้อมูล (Mock Mode)", fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                                Text("ทดสอบแอปได้โดยไม่ต้องรัน PostgreSQL", fontSize = 12.sp, color = Color.Gray)
                            }
                        }

                        Switch(
                            checked = isMockMode,
                            onCheckedChange = { checked ->
                                isMockMode = checked
                                SessionManager.setMockMode(checked)
                                Toast.makeText(
                                    context,
                                    if (checked) "เปิดโหมด Mock สำเร็จ" else "ปิดโหมด Mock (เชื่อมต่อ API จริง)",
                                    Toast.LENGTH_SHORT
                                ).show()
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.Black, checkedTrackColor = Color.LightGray)
                        )
                    }
                }
            }

            // About App
            Text("เกี่ยวกับแอปพลิเคชัน", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Gray)

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("เวอร์ชัน", fontSize = 13.sp, color = Color.Gray)
                        Text("1.0.0 (Build 1)", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("สถาปัตยกรรม", fontSize = 13.sp, color = Color.Gray)
                        Text("Kotlin Jetpack Compose + MVVM", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Backend Stack", fontSize = 13.sp, color = Color.Gray)
                        Text("Express.js + PostgreSQL", fontSize = 13.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Logout Button
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ออกจากระบบ", fontWeight = FontWeight.Bold, color = Color.Red)
            }
        }
    }

    // Edit Base URL Dialog
    if (showEditUrlDialog) {
        var inputUrl by remember { mutableStateOf(currentBaseUrl) }
        AlertDialog(
            onDismissRequest = { showEditUrlDialog = false },
            title = { Text("แก้ไข Backend URL", fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("ตั้งค่า Host ของเซิร์ฟเวอร์ Express:", fontSize = 13.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = inputUrl,
                        onValueChange = { inputUrl = it },
                        label = { Text("API Base URL") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val clean = if (inputUrl.endsWith("/")) inputUrl else "$inputUrl/"
                        SessionManager.setBaseUrl(clean)
                        currentBaseUrl = clean
                        showEditUrlDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Black)
                ) {
                    Text("บันทึก")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditUrlDialog = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }

    // Logout Confirmation Dialog
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("ยืนยันการออกจากระบบ", fontWeight = FontWeight.Bold) },
            text = { Text("คุณต้องการออกจากระบบบัญชีนี้ใช่หรือไม่?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        authViewModel.logout()
                        navController.navigate("signin") {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("ออกจากระบบ")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("ยกเลิก")
                }
            }
        )
    }
}
