package com.example.adidusshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.adidusshop.screens.CheckoutScreen
import com.example.adidusshop.screens.LoginScreen
import com.example.adidusshop.screens.MainScreen
import com.example.adidusshop.screens.ProductDetail
import com.example.adidusshop.screens.SignUpScreen
import com.example.adidusshop.services.SessionManager
import com.example.adidusshop.ui.theme.AdidusShopTheme
import com.example.adidusshop.viewModels.AuthViewModel
import com.example.adidusshop.viewModels.CartViewModel
import com.example.adidusshop.viewModels.OrderViewModel
import com.example.adidusshop.viewModels.ProductViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SessionManager.init(applicationContext)
        enableEdgeToEdge()

        setContent {
            AdidusShopTheme {
                val navController = rememberNavController()

                val authViewModel: AuthViewModel = viewModel()
                val productViewModel: ProductViewModel = viewModel()
                val cartViewModel: CartViewModel = viewModel()
                val orderViewModel: OrderViewModel = viewModel()

                val startDest = if (SessionManager.isLoggedIn()) "home" else "signin"

                NavHost(
                    navController = navController,
                    startDestination = startDest
                ) {
                    composable("signin") {
                        LoginScreen(
                            onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("signin") { inclusive = true }
                                }
                            },
                            onGoToRegister = {
                                navController.navigate("signup")
                            }
                        )
                    }

                    composable("signup") {
                        SignUpScreen(
                            onSignupSuccess = {
                                navController.navigate("signin") {
                                    popUpTo("signup") { inclusive = true }
                                }
                            }
                        )
                    }

                    composable("home") {
                        MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            initialTab = 0
                        )
                    }

                    composable("cart") {
                        MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            initialTab = 1
                        )
                    }

                    composable("orders") {
                        MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            initialTab = 2
                        )
                    }

                    composable("profile") {
                        MainScreen(
                            navController = navController,
                            authViewModel = authViewModel,
                            productViewModel = productViewModel,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel,
                            initialTab = 3
                        )
                    }

                    composable("detail/{product_id}") { backStackEntry ->
                        val productId = backStackEntry.arguments
                            ?.getString("product_id")?.toIntOrNull() ?: 0

                        ProductDetail(
                            navController = navController,
                            product_id = productId,
                            viewModel = productViewModel,
                            cartViewModel = cartViewModel
                        )
                    }

                    composable("checkout") {
                        CheckoutScreen(
                            navController = navController,
                            cartViewModel = cartViewModel,
                            orderViewModel = orderViewModel
                        )
                    }
                }
            }
        }
    }
}
