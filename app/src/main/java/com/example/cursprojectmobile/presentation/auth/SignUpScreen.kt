package com.example.cursprojectmobile.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun SignUpScreen(
    viewModel: AuthViewModel,
    onNavigateToSignIn: () -> Unit,
    onSignUpSuccess: () -> Unit,
    onGoogleSignUpSuccess: () -> Unit = onSignUpSuccess
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Hero Graphic
        Image(
            painter = painterResource(id = R.drawable.onboarding_food2),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Center Handle pill
        Box(
            modifier = Modifier
                .width(48.dp)
                .height(5.dp)
                .clip(RoundedCornerShape(3.dp))
                .background(Color(0xFFEDEDED))
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Top Navigation Tabs (Create Account | Login) matching Figma
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 40.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Create Account",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .width(90.dp)
                        .height(3.dp)
                        .background(BrandPrimary, shape = RoundedCornerShape(2.dp))
                )
            }

            Text(
                text = "Login",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextGray,
                modifier = Modifier.clickable { onNavigateToSignIn() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        ) {
            Text(
                text = "Full Name",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.fullName,
                onValueChange = { viewModel.onFullNameChange(it) },
                placeholder = { Text("Emmanuel Kamcy", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE8E8E8)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Email address",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.signUpEmail,
                onValueChange = { viewModel.onSignUpEmailChange(it) },
                placeholder = { Text("ekamcy@gmail.com", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE8E8E8)
                )
            )

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Password",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextDark
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.signUpPassword,
                onValueChange = { viewModel.onSignUpPasswordChange(it) },
                placeholder = { Text("•••• •••• ••••", color = Color.Gray, fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (uiState.isSignUpPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    IconButton(onClick = { viewModel.toggleSignUpPasswordVisibility() }) {
                        Icon(
                            imageVector = if (uiState.isSignUpPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = "Toggle password visibility",
                            tint = Color.Gray
                        )
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE8E8E8)
                )
            )

            if (uiState.signUpError != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = uiState.signUpError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 13.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Create Account Button
            Button(
                onClick = { viewModel.register(onSuccess = onSignUpSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                enabled = !uiState.isSignUpLoading
            ) {
                if (uiState.isSignUpLoading) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Sign Up",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sign Up with Google Button
            Button(
                onClick = { viewModel.loginWithGoogle(onSuccess = onGoogleSignUpSuccess) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF6F6F6)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Logo",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Sign up with Google",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
