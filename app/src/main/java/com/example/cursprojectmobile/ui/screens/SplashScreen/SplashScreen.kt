package com.example.cursprojectmobile.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
fun SplashScreen(
    onNavigateNext: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(2000L)
        onNavigateNext()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        //Image(
            //painter = painterResource(id = R.drawable.splashscreen),
            //contentDescription = "Splash Screen",
            //modifier = Modifier.fillMaxSize(),
            //contentScale = ContentScale.Crop
        //)
        Text(text = "Loading...", fontSize = 24.sp, color = Color.Black)
    }
}