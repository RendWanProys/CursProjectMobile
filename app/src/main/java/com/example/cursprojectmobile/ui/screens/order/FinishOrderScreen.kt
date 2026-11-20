package com.example.cursprojectmobile.ui.screens.order

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.repository.OrderRepository

val PrimaryRed = Color(0xFFD61355)
val SoftPink = Color(0xFFFFF0F3)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF757575)

@Composable
fun FinishOrderScreen(
    orderId: Long,
    orderRepository: OrderRepository,
    onNavigateHome: () -> Unit
) {
    val context = LocalContext.current
    var rating by remember { mutableIntStateOf(4) }
    var feedback by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Success Circle Icon
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(PrimaryRed),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "✓", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = "Thank You!\nOrder Completed",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 30.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Please rate your last Driver",
            fontSize = 14.sp,
            color = TextGray
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 5 Star Rating Row
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                Text(
                    text = if (i <= rating) "⭐" else "☆",
                    fontSize = 32.sp,
                    modifier = Modifier.clickable { rating = i }
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Feedback input
        OutlinedTextField(
            value = feedback,
            onValueChange = { feedback = it },
            modifier = Modifier
                .fillMaxWidth()
                .height(110.dp),
            placeholder = { Text("Leave feedback...", color = Color(0xFFAAAAAA), fontSize = 14.sp) },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryRed,
                unfocusedBorderColor = Color(0xFFE8E8E8)
            )
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Action Buttons Row (Submit / Skip)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    if (orderId > 0) {
                        orderRepository.submitRating(orderId, rating, feedback)
                    }
                    Toast.makeText(context, "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                    onNavigateHome()
                },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed)
            ) {
                Text("Submit", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            OutlinedButton(
                onClick = onNavigateHome,
                modifier = Modifier
                    .weight(0.7f)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextGray),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
            ) {
                Text("Skip", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
        }
    }
}
