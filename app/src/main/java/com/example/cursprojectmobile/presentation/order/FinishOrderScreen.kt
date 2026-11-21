package com.example.cursprojectmobile.presentation.order

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.shadow
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
        // Top food doodle background pattern
        Image(
            painter = painterResource(id = R.drawable.bg_food_pattern),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .align(Alignment.TopCenter),
            contentScale = ContentScale.Crop,
            alpha = 0.55f
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Checkmark in Pink Circle with decorative dots
            Box(
                modifier = Modifier
                    .size(92.dp)
                    .clip(CircleShape)
                    .background(BrandPrimary)
                    .shadow(8.dp, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "✓",
                    fontSize = 44.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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

            Text(
                text = "Please rate your last Driver",
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = TextGray,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 5 Solid Gold Stars
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (star in 1..5) {
                    val isSelected = star <= uiState.rating
                    Text(
                        text = "★",
                        fontSize = 38.sp,
                        color = if (isSelected) Color(0xFFFFB800) else Color(0xFFFFE0B2),
                        modifier = Modifier
                            .clickable { viewModel.setRating(star) }
                            .padding(2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Feedback Input Field with Pencil Icon matching mockup
            OutlinedTextField(
                value = uiState.feedback,
                onValueChange = { viewModel.setFeedback(it) },
                placeholder = {
                    Text(
                        text = "Leave feedback",
                        color = Color(0xFF9E9E9E),
                        fontSize = 14.sp,
                        fontFamily = Poppins
                    )
                },
                leadingIcon = {
                    Text(text = "✏️", fontSize = 16.sp)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = BrandPrimary,
                    unfocusedBorderColor = Color(0xFFE8E8E8),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Action Buttons Row: [ Submit (Pink) ] and [ Skip (White with Pink text) ]
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        viewModel.submitFeedback {
                            onNavigateToHome()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text(
                        text = "Submit",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = Color.White
                    )
                }

                Surface(
                    onClick = { onNavigateToHome() },
                    modifier = Modifier
                        .width(96.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color(0xFFFAFAFA),
                    border = BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "Skip",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins,
                            color = BrandPrimary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
