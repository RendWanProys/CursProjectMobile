package com.example.cursprojectmobile.presentation.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun SetLocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit,
    onLocationSet: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFDE8EC))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\u2190",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = BrandPrimary
                )
            }

            Text(
                text = "Set Your Location",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFDE8EC)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "\uD83D\uDD14", fontSize = 18.sp)
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Map Preview matching Image 5
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .border(1.dp, Color(0xFFE8E8E8), RoundedCornerShape(20.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.map_preview),
                    contentDescription = "Map Location",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Center Map Pin overlay matching Figma Image 5
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(BrandPrimary)
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Text("\uD83D\uDCCD", fontSize = 22.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Your Current Delivery Location",
                fontSize = 13.sp,
                color = TextGray,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Current Address display
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 52.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = uiState.selectedAddress,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark,
                    lineHeight = 24.sp,
                    minLines = 2,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = "Select From Saved Addresses",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Preset Addresses matching Figma Image 5 exactly
            uiState.presetLocations.forEach { loc ->
                val isSelected = loc.address.equals(uiState.selectedAddress, ignoreCase = true)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { viewModel.selectAddress(loc.address) },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFFFFF0F3) else Color(0xFFF9F9F9)
                    ),
                    border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, BrandPrimary) else null
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isSelected) "\uD83D\uDCCD" else "\uD83C\uDFE1",
                            fontSize = 20.sp
                        )

                        Spacer(modifier = Modifier.width(14.dp))

                        Text(
                            text = loc.address,
                            fontSize = 14.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontFamily = Poppins,
                            color = TextDark,
                            lineHeight = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom Confirm Button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Box(modifier = Modifier.padding(20.dp)) {
                Button(
                    onClick = {
                        viewModel.confirmLocation {
                            onLocationSet(uiState.selectedAddress)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text(
                        text = "Confirm Location",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = Color.White
                    )
                }
            }
        }
    }
}
