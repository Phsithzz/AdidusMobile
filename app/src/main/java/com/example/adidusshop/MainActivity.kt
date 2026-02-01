package com.example.adidusshop

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.adidusshop.screens.HomeProduct
import com.example.adidusshop.screens.LoginScreen
import com.example.adidusshop.screens.SignUpScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController, startDestination = "home"
            ){
                 composable("signin"){
                     LoginScreen(
                         onLoginSuccess = {
                             navController.navigate("home"){
                                 popUpTo("signin"){inclusive=true}
                             }
                         },
                         onGoToRegister = {
                             navController.navigate("signup")
                         }
                     )
                 }
                composable("signup"){
                    SignUpScreen(
                        onSignupSuccess = {
                            navController.navigate("signin"){
                                popUpTo("signup"){inclusive=true}
                            }
                        }
                    )
                }
                composable("home"){
                    HomeProduct()
                }




            }
        }
    }
}

