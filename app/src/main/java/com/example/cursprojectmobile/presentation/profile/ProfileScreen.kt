package com.example.cursprojectmobile.presentation.profile

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import androidx.compose.ui.layout.ContentScale
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel,
    cartItemCount: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToChat: () -> Unit,
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val user = uiState.user
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadProfile()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = 1,
                cartItemCount = cartItemCount,
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> onNavigateToHome()
                        1 -> {} // Already on Profile
                        2 -> onNavigateToCart()
                        3 -> onNavigateToChat()
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_food_pattern),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Crop,
                alpha = 0.45f
            )

            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Header
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
                            .clickable { onNavigateToHome() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "‹",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            color = BrandPrimary
                        )
                    }

                    Text(
                        text = "Profile",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )

                    Text(
                        text = if (uiState.isEditMode) "Save" else "Edit",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary,
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color(0xFFFDE8EC))
                            .clickable {
                                if (uiState.isEditMode) {
                                    viewModel.saveProfile {
                                        Toast.makeText(context, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    viewModel.toggleEditMode()
                                }
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    )
                }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // User Avatar and change button
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(contentAlignment = Alignment.BottomEnd) {
                            val avatarEmoji = user?.avatarPath?.ifEmpty { "👩" } ?: "👩"
                            val isDefaultAvatar = user?.avatarPath.isNullOrEmpty() || user?.avatarPath == "avatar_default"
                            
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .clip(CircleShape)
                                    .background(BrandLightPink)
                                    .clickable { viewModel.setAvatarPickerOpen(true) },
                                contentAlignment = Alignment.Center
                            ) {
                                if (isDefaultAvatar) {
                                    FoodImagePlaceholder(
                                        drawableRes = FoodImages.USER_AVATAR,
                                        emoji = "👩",
                                        cornerRadius = 48.dp,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                } else {
                                    Text(text = avatarEmoji, fontSize = 48.sp)
                                }
                            }

                            // Edit pencil badge
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimary)
                                    .clickable { viewModel.setAvatarPickerOpen(true) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text("✏️", fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = user?.fullName?.ifEmpty { "Anamika Sandula" } ?: "Anamika Sandula",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )

                        Text(
                            text = user?.email?.ifEmpty { "anamika@gmail.com" } ?: "anamika@gmail.com",
                            fontSize = 13.sp,
                            color = TextGray
                        )
                    }
                }

                // Profile Fields or Edit Mode
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            if (uiState.isEditMode) {
                                OutlinedTextField(
                                    value = uiState.editFullName,
                                    onValueChange = { viewModel.onEditFullName(it) },
                                    label = { Text("Full Name") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = uiState.editPhone,
                                    onValueChange = { viewModel.onEditPhone(it) },
                                    label = { Text("Phone Number") },
                                    placeholder = { Text("+1 (555) 000-0000", color = Color.Gray) },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = uiState.editAddress,
                                    onValueChange = { viewModel.onEditAddress(it) },
                                    label = { Text("Delivery Address") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )
                            } else {
                                val phoneDisplay = user?.phone?.ifEmpty { null } ?: "Not specified (tap to add)"
                                ProfileInfoRow(
                                    drawableResId = R.drawable.ic_phone,
                                    label = "Phone",
                                    value = phoneDisplay,
                                    isHighlight = user?.phone.isNullOrEmpty(),
                                    onClick = { if (user?.phone.isNullOrEmpty()) viewModel.toggleEditMode() }
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF2F2F2))
                                ProfileInfoRow(
                                    drawableResId = R.drawable.ic_location,
                                    label = "Delivery Address",
                                    value = user?.address?.ifEmpty { "4517 Washington Ave. Manchester, Kentucky 39495" } ?: "4517 Washington Ave."
                                )
                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF2F2F2))
                                ProfileInfoRow(
                                    drawableResId = R.drawable.ic_package,
                                    label = "Completed Orders",
                                    value = "${uiState.orders.size} orders"
                                )
                            }
                        }
                    }
                }

                // Order History Section
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Recent Orders",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }

                if (uiState.orders.isEmpty()) {
                    item {
                        Text(
                            text = "No past orders yet. Place your first order today!",
                            color = TextGray,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                } else {
                    items(uiState.orders) { order ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    val formattedDate = java.text.SimpleDateFormat("dd MMM, HH:mm", java.util.Locale.getDefault()).format(java.util.Date(order.date))
                                    Text(
                                        text = "Order #${order.id} • $formattedDate",
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 14.sp,
                                        color = TextDark
                                    )
                                    Text(
                                        text = "Status: ${order.status} • Delivery to: ${order.address.take(20)}...",
                                        fontSize = 12.sp,
                                        color = TextGray
                                    )
                                }
                                Text(
                                    text = "$${"%.2f".format(order.totalAmount)}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = BrandPrimary
                                )
                            }
                        }
                    }
                }

                // Logout Button
                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    OutlinedButton(
                        onClick = { viewModel.logout(onLogout) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                    ) {
                        Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                }
            }
        }
    }
    }

    // Avatar Picker Dialog
    if (uiState.isAvatarPickerOpen) {
        val avatarChoices = listOf("👩", "👨‍🍳", "🛵", "😎", "👧", "🧔", "🍔", "🍕")
        AlertDialog(
            onDismissRequest = { viewModel.setAvatarPickerOpen(false) },
            title = {
                Text(
                    text = "Choose Your Avatar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextDark
                )
            },
            text = {
                Column {
                    Text(
                        text = "Select an avatar icon to personalize your profile:",
                        fontSize = 13.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        avatarChoices.take(4).forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(if (user?.avatarPath == emoji) BrandLightPink else Color(0xFFF3F3F3))
                                    .border(
                                        width = if (user?.avatarPath == emoji) 2.dp else 0.dp,
                                        color = BrandPrimary,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.selectAvatar(emoji) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 28.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        avatarChoices.drop(4).forEach { emoji ->
                            Box(
                                modifier = Modifier
                                    .size(52.dp)
                                    .clip(CircleShape)
                                    .background(if (user?.avatarPath == emoji) BrandLightPink else Color(0xFFF3F3F3))
                                    .border(
                                        width = if (user?.avatarPath == emoji) 2.dp else 0.dp,
                                        color = BrandPrimary,
                                        shape = CircleShape
                                    )
                                    .clickable { viewModel.selectAvatar(emoji) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 28.sp)
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { viewModel.selectAvatar("avatar_default") }) {
                    Text("Reset to Photo", color = BrandPrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { viewModel.setAvatarPickerOpen(false) }) {
                    Text("Cancel", color = TextGray)
                }
            }
        )
    }
}

@Composable
private fun ProfileInfoRow(
    drawableResId: Int,
    label: String,
    value: String,
    isHighlight: Boolean = false,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isHighlight) { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF5F5F5)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = drawableResId),
                contentDescription = label,
                modifier = Modifier.size(20.dp),
                colorFilter = ColorFilter.tint(TextGray)
            )
        }
        Spacer(modifier = Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = TextGray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (isHighlight) BrandPrimary else TextDark
            )
        }
    }
}
