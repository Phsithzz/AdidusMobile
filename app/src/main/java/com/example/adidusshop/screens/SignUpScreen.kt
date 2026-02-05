package com.example.adidusshop.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.adidusshop.R
import com.example.adidusshop.viewModels.AuthViewModel
import com.example.adidusshop.viewModels.RegisterState

@Composable
fun SignUpScreen(
    onSignupSuccess: () -> Unit
){
    var name by remember{ mutableStateOf("") }
    var lastname by remember {mutableStateOf("")}
    var email by remember {mutableStateOf("")}
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }

    val authViewModel: AuthViewModel = viewModel()
    val registerState by authViewModel.registerState.collectAsState()

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(registerState) {
        if (registerState is RegisterState.Success){
            onSignupSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffF5F2F2))
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Image(
            modifier = Modifier.size(100.dp),
            painter = painterResource(id = R.drawable.iconmobile),
            contentDescription = "IconAdidus"

        )

        Text(
            text = "Welcome To My Shop",
            fontSize = 36.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(10.dp))


        Spacer(modifier = Modifier.height(24.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            )

        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Name",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = name,
                    onValueChange = {name = it},
                    placeholder = {Text(
                        text = "Enter your Name"
                    )},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Icon person",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "LastName",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = lastname,
                    onValueChange = {lastname = it},
                    placeholder = {Text(
                        text = "Enter your Lastname"
                    )},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Icon person",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Email",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = email,
                    onValueChange = {email = it},
                    placeholder = {Text(
                        text = "Enter your Email"
                    )},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Icon Email",
                            tint = Color.Gray
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = password,
                    onValueChange = {password = it},
                    placeholder = {Text(
                        text = "Enter your Password"
                    )},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Icon Lock",
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector =
                                    if (passwordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle password visibility"
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Gray
                    ),
                    visualTransformation =
                        if (passwordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),

                )
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Confirm Password",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(10.dp))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = confirmPassword,
                    onValueChange = {confirmPassword = it},
                    placeholder = {Text(
                        text = "Enter your Password Again"
                    )},
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Icon Lock",
                            tint = Color.Gray
                        )
                    },
                    trailingIcon = {
                        IconButton(onClick = {
                            confirmPasswordVisible = !confirmPasswordVisible
                        }) {
                            Icon(
                                imageVector =
                                    if (confirmPasswordVisible) Icons.Default.Visibility
                                    else Icons.Default.VisibilityOff,
                                contentDescription = "Toggle confirm password visibility"
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Green,
                        unfocusedBorderColor = Color.Gray
                    ),
                    visualTransformation =
                        if (confirmPasswordVisible) VisualTransformation.None
                        else PasswordVisualTransformation(),

                )
                Spacer(modifier = Modifier.height(16.dp))


                Column(
                    modifier = Modifier
                        .fillMaxWidth(),

                ) {
                    Text(
                        text = message,
                        fontSize = 16.sp,
                        color = Color.Red,
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onClick = {onSignupSuccess() }
                    ) {
                        Text(
                            text = "Sign In Here",
                            fontSize = 16.sp,
                            color = Color.Black,

                        )
                    }

                }
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        authViewModel.register(name,lastname,email, password,confirmPassword)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Black
                    )
                ) {
                    if (registerState is RegisterState.Loading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = "Sign Up",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

            }
        }

        when (registerState) {
            is RegisterState.Idle -> {

            }

            is RegisterState.Loading -> {

            }

            is RegisterState.Error -> {
                message = (registerState as RegisterState.Error).message
            }
            is RegisterState.Success -> {

            }

            else -> {}
        }

    }
}
