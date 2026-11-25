package com.example.cursprojectmobile.presentation.notifications

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.DoneAll
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
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

private val SoftPinkBadge = Color(0xFFFDE8EC)

data class NotificationItem(
    val id: Long,
    val title: String,
    val description: String,
    val time: String,
    val iconEmoji: String,
    val category: String,
    var isRead: Boolean = false
)

@Composable
fun NotificationsScreen(
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var selectedTab by remember { mutableStateOf("All") }

    val initialNotifications = remember {
        mutableStateListOf(
            NotificationItem(
                id = 1,
                title = "Your food is on the way!",
                description = "Courier Courtney Henry has picked up your Chicken Burger Promo Pack. Estimated arrival: 15 mins.",
                time = "10m ago",
                iconEmoji = "🛵",
                category = "Orders",
                isRead = false
            ),
            NotificationItem(
                id = 2,
                title = "Special Deal for Today!",
                description = "Free delivery on all orders over $20. Don't miss out on promo packs this weekend!",
                time = "1h ago",
                iconEmoji = "🎉",
                category = "Promos",
                isRead = false
            ),
            NotificationItem(
                id = 3,
                title = "Order #2 Delivered",
                description = "Your order was successfully delivered to 4517 Washington Ave. Please rate your experience.",
                time = "Yesterday",
                iconEmoji = "✅",
                category = "Orders",
                isRead = true
            ),
            NotificationItem(
                id = 4,
                title = "Payment Successful",
                description = "Payment of $39.00 via Google Pay was confirmed for Order #2.",
                time = "Yesterday",
                iconEmoji = "💳",
                category = "Orders",
                isRead = true
            ),
            NotificationItem(
                id = 5,
                title = "New Menu Item: Spicy Shawarma!",
                description = "Try our new chef special: juicy grilled shawarma with smoked spices and garlic sauce.",
                time = "3 days ago",
                iconEmoji = "🌯",
                category = "Promos",
                isRead = true
            )
        )
    }

    val filteredNotifications = remember(selectedTab, initialNotifications.toList()) {
        when (selectedTab) {
            "Orders" -> initialNotifications.filter { it.category == "Orders" }
            "Promos" -> initialNotifications.filter { it.category == "Promos" }
            else -> initialNotifications
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // --- TOP BAR ---
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

            Text(
                text = "Notifications",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark
            )

            // Mark all read action
            TextButton(
                onClick = {
                    initialNotifications.forEachIndexed { index, item ->
                        initialNotifications[index] = item.copy(isRead = true)
                    }
                    Toast.makeText(context, "All notifications marked as read", Toast.LENGTH_SHORT).show()
                }
            ) {
                Text(
                    text = "Read all",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = BrandPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // --- FILTER CHIPS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            listOf("All", "Orders", "Promos").forEach { tab ->
                val isSelected = selectedTab == tab
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isSelected) BrandPrimary else SoftPinkBadge)
                        .clickable { selectedTab = tab }
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = tab,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = if (isSelected) Color.White else BrandPrimary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- NOTIFICATIONS LIST ---
        if (filteredNotifications.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🔔", fontSize = 54.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "No Notifications",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "You're all caught up with your orders and promos!",
                        fontSize = 13.sp,
                        fontFamily = Poppins,
                        color = TextGray
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredNotifications, key = { it.id }) { notif ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                val idx = initialNotifications.indexOfFirst { it.id == notif.id }
                                if (idx != -1) {
                                    initialNotifications[idx] = notif.copy(isRead = true)
                                }
                            },
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (notif.isRead) Color.White else Color(0xFFFFF9FA)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            // Emoji badge
                            Box(
                                modifier = Modifier
                                    .size(46.dp)
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(SoftPinkBadge),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = notif.iconEmoji, fontSize = 22.sp)
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = notif.title,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = Poppins,
                                        color = TextDark,
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (!notif.isRead) {
                                        Box(
                                            modifier = Modifier
                                                .size(8.dp)
                                                .clip(CircleShape)
                                                .background(BrandPrimary)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                Text(
                                    text = notif.description,
                                    fontSize = 13.sp,
                                    fontFamily = Poppins,
                                    color = TextGray,
                                    lineHeight = 18.sp
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = notif.time,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Poppins,
                                    color = Color(0xFFA0A5BA)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
