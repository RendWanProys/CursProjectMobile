package com.example.cursprojectmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.ui.screens.OnboardingScreen
import com.example.cursprojectmobile.ui.screens.auth.LoginScreen
import com.example.cursprojectmobile.ui.screens.auth.RegisterScreen
import com.example.cursprojectmobile.ui.screens.splash.SplashScreen
import com.example.cursprojectmobile.ui.theme.CursProjectMobileTheme
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CursProjectMobileTheme {
                AppRoot()
            }
        }
    }
}

@Composable
fun AppRoot() {
    var currentScreen by remember { mutableStateOf("splash") }

    when (currentScreen) {
        "splash" -> {
            SplashScreen(
                onNavigateNext = {
                    val user = FirebaseAuth.getInstance().currentUser
                    currentScreen = if (user != null) "home" else "onboarding"
                }
            )
        }
        "onboarding" -> {
            OnboardingScreen(
                onFinishOnboarding = { currentScreen = "register" }
            )
        }
        "register" -> {
            RegisterScreen(
                onNavigateToLogin = { currentScreen = "login" },
                onRegisterSuccess = { currentScreen = "home" }
            )
        }
        "login" -> {
            LoginScreen(
                onNavigateToRegister = { currentScreen = "register" },
                onLoginSuccess = { currentScreen = "home" }
            )
        }
        "home" -> {
            val user = FirebaseAuth.getInstance().currentUser
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome, ${user?.displayName ?: user?.email ?: "User"}!",
                    fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = {
                    FirebaseAuth.getInstance().signOut()
                    currentScreen = "login"
                }) {
                    Text("Log Out")
                }
            }
        }
    }
}