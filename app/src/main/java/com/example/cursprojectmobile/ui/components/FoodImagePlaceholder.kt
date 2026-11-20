package com.example.cursprojectmobile.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Универсальный компонент для картинок и заглушек.
 * Если передан валидный [drawableRes], отображает настоящее изображение.
 * Если передан null или ресурс отсутствует, отображает эстетичную заглушку с градиентом и иконкой/эмодзи.
 */
@Composable
fun FoodImagePlaceholder(
    modifier: Modifier = Modifier,
    drawableRes: Int? = null,
    emoji: String = "🍔",
    cornerRadius: Dp = 16.dp,
    contentDescription: String? = null,
    contentScale: ContentScale = ContentScale.Crop,
    fontSize: TextUnit = 32.sp
) {
    val shape = RoundedCornerShape(cornerRadius)

    if (drawableRes != null && drawableRes != 0) {
        Image(
            painter = painterResource(id = drawableRes),
            contentDescription = contentDescription,
            modifier = modifier
                .clip(shape),
            contentScale = contentScale
        )
    } else {
        Box(
            modifier = modifier
                .clip(shape)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFFFF0F3),
                            Color(0xFFFFD6E0)
                        )
                    )
                )
                .border(1.dp, Color(0xFFFFB3C6), shape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = emoji,
                fontSize = fontSize
            )
        }
    }
}
