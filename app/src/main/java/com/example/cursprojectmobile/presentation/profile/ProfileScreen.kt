package com.example.cursprojectmobile.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages

private val PrimaryRed = Color(0xFFD61355)
private val SoftPink = Color(0xFFFFF0F3)
private val TextDark = Color(0xFF1F1F1F)
private val TextGray = Color(0xFF757575)

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAFAFA))
                .statusBarsPadding()
                .padding(innerPadding)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Text(
                    text = if (uiState.isEditMode) "Save" else "Edit",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed,
                    modifier = Modifier.clickable {
                        if (uiState.isEditMode) {
                            viewModel.saveProfile()
                        } else {
                            viewModel.toggleEditMode()
                        }
                    }
                )
            }

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                // User Avatar and info header
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        FoodImagePlaceholder(
                            drawableRes = FoodImages.USER_AVATAR,
                            emoji = "👩",
                            cornerRadius = 45.dp,
                            modifier = Modifier.size(90.dp)
                        )

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
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = uiState.editPhone,
                                    onValueChange = { viewModel.onEditPhone(it) },
                                    label = { Text("Phone") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                OutlinedTextField(
                                    value = uiState.editAddress,
                                    onValueChange = { viewModel.onEditAddress(it) },
                                    label = { Text("Delivery Address") },
                                    modifier = Modifier.fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                            } else {
                                ProfileInfoRow(icon = "📱", label = "Phone", value = user?.phone?.ifEmpty { "+1 234 567 890" } ?: "+1 234 567 890")
                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF2F2F2))
                                ProfileInfoRow(icon = "📍", label = "Delivery Address", value = user?.address?.ifEmpty { "4517 Washington Ave. Manchester, Kentucky 39495" } ?: "4517 Washington Ave.")
                                HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp), color = Color(0xFFF2F2F2))
                                ProfileInfoRow(icon = "📦", label = "Completed Orders", value = "${uiState.orders.size} orders")
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
                                    fontSize = 16.sp,
                                    color = PrimaryRed
                                )
                            }
                        }
                    }
                }

                // Logout Button
                item {
                    Spacer(modifier = Modifier.height(28.dp))
                    Button(
                        onClick = { viewModel.logout { onLogout() } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFF0F3))
                    ) {
                        Text(
                            text = "Sign Out",
                            color = PrimaryRed,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ProfileInfoRow(icon: String, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = icon, fontSize = 20.sp)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 12.sp, color = TextGray)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextDark)
        }
    }
}
