package com.example.cursprojectmobile.presentation.location

import android.widget.Toast
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

private val SoftPinkBadge = Color(0xFFFDE8EC)

@Composable
fun SetLocationScreen(
    viewModel: LocationViewModel,
    onBack: () -> Unit,
    onLocationSet: (String) -> Unit,
    onNavigateToNotifications: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    var mapSize by remember { mutableStateOf(IntSize.Zero) }

    // Animated pin coordinates
    val animatedPinX by animateFloatAsState(
        targetValue = uiState.pinXPct,
        animationSpec = tween(durationMillis = 300),
        label = "pinX"
    )
    val animatedPinY by animateFloatAsState(
        targetValue = uiState.pinYPct,
        animationSpec = tween(durationMillis = 300),
        label = "pinY"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF6F6F6))
    ) {
        // --- 1. FULLSCREEN INTERACTIVE MAP ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { mapSize = it }
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        if (mapSize.width > 0 && mapSize.height > 0) {
                            val xPct = offset.x / mapSize.width
                            val yPct = offset.y / mapSize.height
                            viewModel.onMapTapped(xPct, yPct)
                        }
                    }
                }
        ) {
            Image(
                painter = painterResource(id = R.drawable.maps_fullscreen),
                contentDescription = "Interactive Street Map",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Pin with radar halo positioned at animated location
            if (mapSize.width > 0 && mapSize.height > 0) {
                val pinPixelX = (animatedPinX * mapSize.width)
                val pinPixelY = (animatedPinY * mapSize.height)

                // Halo outer circle
                Box(
                    modifier = Modifier
                        .offset(
                            x = (pinPixelX / context.resources.displayMetrics.density).dp - 110.dp,
                            y = (pinPixelY / context.resources.displayMetrics.density).dp - 110.dp
                        )
                        .size(220.dp)
                        .clip(CircleShape)
                        .background(BrandPrimary.copy(alpha = 0.12f))
                )

                // Halo inner circle
                Box(
                    modifier = Modifier
                        .offset(
                            x = (pinPixelX / context.resources.displayMetrics.density).dp - 36.dp,
                            y = (pinPixelY / context.resources.displayMetrics.density).dp - 36.dp
                        )
                        .size(72.dp)
                        .clip(CircleShape)
                        .background(BrandPrimary.copy(alpha = 0.28f)),
                    contentAlignment = Alignment.Center
                ) {
                    // Center pin icon
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Pin Location",
                        tint = BrandPrimary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // --- 2. TOP FLOATING CONTROLS ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Top Bar: Back squircle button and Notification bell squircle button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftPinkBadge)
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = BrandPrimary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(SoftPinkBadge)
                        .clickable { onNavigateToNotifications() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notifications",
                        tint = BrandPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Floating Search Bar ("Where is your location")
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFFFF0F3),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCE0E6)),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = BrandPrimary,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(modifier = Modifier.weight(1f)) {
                        if (uiState.searchQuery.isEmpty()) {
                            Text(
                                text = "Where is your location",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins,
                                color = TextDark.copy(alpha = 0.85f)
                            )
                        }
                        BasicTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChanged(it) },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Poppins,
                                color = TextDark
                            ),
                            cursorBrush = SolidColor(BrandPrimary),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (uiState.searchQuery.isNotEmpty()) {
                        IconButton(
                            onClick = { viewModel.onSearchQueryChanged("") },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = TextGray,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // --- 3. FLOATING "MY LOCATION" GPS FAB ---
        FloatingActionButton(
            onClick = {
                Toast.makeText(context, "Locating your position via GPS...", Toast.LENGTH_SHORT).show()
                viewModel.detectDeviceLocation(context)
            },
            containerColor = Color.White,
            contentColor = BrandPrimary,
            shape = CircleShape,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 20.dp, bottom = 225.dp)
                .size(50.dp)
                .shadow(6.dp, CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.MyLocation,
                contentDescription = "My GPS Location",
                modifier = Modifier.size(24.dp)
            )
        }

        // --- 4. FLOATING BOTTOM SHEET WITH ADDRESS & SET LOCATION ---
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Your Location",
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    color = TextGray,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SoftPinkBadge),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Pin",
                            tint = BrandPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = uiState.selectedAddress,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = TextDark,
                        lineHeight = 22.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Big Red "Set Location" Button
                Button(
                    onClick = {
                        viewModel.confirmLocation {
                            Toast.makeText(context, "Delivery location set to: ${uiState.selectedAddress}", Toast.LENGTH_SHORT).show()
                            onLocationSet(uiState.selectedAddress)
                            onBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text(
                        text = "Set Location",
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
