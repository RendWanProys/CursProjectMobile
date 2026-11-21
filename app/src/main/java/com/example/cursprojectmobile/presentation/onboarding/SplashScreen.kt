package com.example.cursprojectmobile.presentation.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.data.UserSessionManager
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    viewModel: OnboardingViewModel,
    onNavigateToWelcome: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    // Timer starts only after the splash screen composable is actually rendered on screen
    LaunchedEffect(Unit) {
        delay(3000L)
        if (UserSessionManager.isLoggedIn()) {
            onNavigateToHome()
        } else {
            onNavigateToWelcome()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Fullscreen splash illustration with scooter delivery girl and "Food Couriers" logo
        Image(
            painter = painterResource(id = R.drawable.splashscreen),
            contentDescription = "Food Couriers Splash",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}
