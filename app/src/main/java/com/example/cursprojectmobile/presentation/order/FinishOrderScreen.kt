package com.example.cursprojectmobile.presentation.order

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun FinishOrderScreen(
    orderId: Long,
    viewModel: OrderViewModel,
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.loadOrder(orderId)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // 1. Confetti / Party Popper in Mint Circle matching Image 7
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFE8F8F2)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\uD83C\uDF89",
                    fontSize = 52.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Title: "Thank You! Order Completed"
            Text(
                text = "Thank You!\nOrder Completed",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark,
                textAlign = TextAlign.Center,
                lineHeight = 32.sp
            )

            Spacer(modifier = Modifier.height(10.dp))

            // 3. Subtitle: "Please rate your driver & food delivery experience"
            Text(
                text = "Please rate your driver & food delivery experience",
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // 4. Driver Profile Card (Courtney Henry) matching Image 7
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFFFC107)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_avatar),
                            contentDescription = "Driver Avatar",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Courtney Henry",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = TextDark
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Food Delivery Partner • 4.9 \u2605",
                            fontSize = 13.sp,
                            fontFamily = Poppins,
                            color = TextGray
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 5. 5 Large Interactive Gold Rating Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (star in 1..5) {
                    val isSelected = star <= uiState.rating
                    Text(
                        text = "\u2605",
                        fontSize = 42.sp,
                        color = if (isSelected) Color(0xFFFFB800) else Color(0xFFFFE0B2),
                        modifier = Modifier
                            .clickable { viewModel.setRating(star) }
                            .padding(2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 6. Feedback text input area matching Image 7
            OutlinedTextField(
                value = uiState.feedback,
                onValueChange = { viewModel.setFeedback(it) },
                placeholder = {
                    Text(
                        text = "Leave feedback for driver...",
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF9E9E9E),
                    unfocusedBorderColor = Color(0xFFE0E0E0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                maxLines = 4
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 7. Full width "Submit & Return Home" pink button
            Button(
                onClick = {
                    viewModel.submitFeedback {
                        onNavigateToHome()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
            ) {
                Text(
                    text = "Submit & Return Home",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 8. Skip link
            Text(
                text = "Skip",
                fontSize = 15.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = TextGray,
                modifier = Modifier
                    .clickable { onNavigateToHome() }
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
