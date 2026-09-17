package com.example.cursprojectmobile.ui.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.cursprojectmobile.R
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    // Задержка 2 секунды перед переходом
    LaunchedEffect(Unit) {
        delay(2000L)
        onNavigateNext()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.splashscreen),
            contentDescription = "Splash Screen",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Заполняет экран без искажения пропорций
        )
    }
}