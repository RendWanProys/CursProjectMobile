package com.example.cursprojectmobile.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.R

val PrimaryPink = Color(0xFFE91E63)
val PrimaryRed = Color(0xFFD61355)
val DotInactive = Color(0xFFE0E0E0)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF6B6B6B)

@Composable
fun OnboardingScreen(
    onNextClicked: () -> Unit,
    onSkipClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Верхняя иллюстрация (еда + фон)
        Image(
            painter = painterResource(id = R.drawable.onboarding_food),
            contentDescription = "Onboarding Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(top = 16.dp),
            contentScale = ContentScale.Fit
        )

        // Блок с текстами
        Text(
            text = "Select the\nFavorites Menu",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            textAlign = TextAlign.Center,
            lineHeight = 32.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Now eat well, don't leave the house, You can\nchoose your favorite food only with\none click",
            fontSize = 14.sp,
            color = TextGray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        // Кнопка Next с градиентом
        Button(
            onClick = onNextClicked,
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(PrimaryPink, PrimaryRed)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Next",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Нижняя строка: Skip | Индикаторы страниц | Стрелка
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Skip",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = TextDark,
                modifier = Modifier.clickable { onSkipClicked() }
            )

            // Точки-индикаторы (вторая активна, как на макете)
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(DotInactive)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(DotInactive)
                )
            }

            Text(
                text = "→",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryRed,
                modifier = Modifier
                    .clickable { onNextClicked() }
                    .padding(4.dp)
            )
        }
    }
}