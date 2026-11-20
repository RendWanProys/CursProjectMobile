package com.example.cursprojectmobile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val BrandPrimary = Color(0xFFD61355)
val BrandLightPink = Color(0xFFFFF0F3)
val BrandInactive = Color(0xFFFF94B2)

@Composable
fun BottomNavBar(
    selectedTab: Int,
    cartItemCount: Int,
    onTabSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Tab 0: Home
        BottomNavItem(
            icon = "🏠",
            label = "Home",
            isSelected = selectedTab == 0,
            onClick = { onTabSelected(0) }
        )

        // Tab 1: Profile
        BottomNavItem(
            icon = "👤",
            label = "Profile",
            isSelected = selectedTab == 1,
            onClick = { onTabSelected(1) }
        )

        // Tab 2: Cart
        BottomNavItem(
            icon = "🛒",
            label = "Cart",
            isSelected = selectedTab == 2,
            badgeCount = cartItemCount,
            onClick = { onTabSelected(2) }
        )

        // Tab 3: Chat
        BottomNavItem(
            icon = "💬",
            label = "Chat",
            isSelected = selectedTab == 3,
            hasNotification = true,
            onClick = { onTabSelected(3) }
        )
    }
}

@Composable
private fun BottomNavItem(
    icon: String,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    hasNotification: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .clickable(onClick = onClick)
            .then(
                if (isSelected) {
                    Modifier
                        .background(BrandLightPink)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                } else {
                    Modifier.padding(8.dp)
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box {
                Text(
                    text = icon,
                    fontSize = 20.sp
                )
                if (badgeCount > 0) {
                    Box(
                        modifier = Modifier
                            .offset(x = 12.dp, y = (-6).dp)
                            .size(18.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (badgeCount > 99) "99+" else badgeCount.toString(),
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (hasNotification) {
                    Box(
                        modifier = Modifier
                            .offset(x = 14.dp, y = (-4).dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary)
                    )
                }
            }

            if (isSelected) {
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = label,
                    color = BrandPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
