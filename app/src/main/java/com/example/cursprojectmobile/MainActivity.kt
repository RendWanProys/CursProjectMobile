package com.example.cursprojectmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.cursprojectmobile.ui.screens.OnboardingScreen
import com.example.cursprojectmobile.ui.screens.splash.SplashScreen
import com.example.cursprojectmobile.ui.theme.CursProjectMobileTheme
import com.example.cursprojectmobile.ui.screens.auth.RegisterScreen
import com.example.cursprojectmobile.ui.screens.auth.LoginScreen



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
                onNavigateNext = { currentScreen = "onboarding" }
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
            // Главный экран
        }
    }
}