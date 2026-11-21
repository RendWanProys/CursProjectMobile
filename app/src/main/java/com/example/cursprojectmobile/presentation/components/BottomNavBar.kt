package com.example.cursprojectmobile.presentation.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.TextGray
import Rg.CursProjectMobile.R

data class NavTab(
    val iconRes: Int,
    val label: String
)

@Composable
fun BottomNavBar(
    selectedTab: Int,
    cartItemCount: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf(
        NavTab(R.drawable.ic_home,   "Home"),
        NavTab(R.drawable.ic_person, "Profile"),
        NavTab(R.drawable.ic_cart,   "Cart"),
        NavTab(R.drawable.ic_chat,   "Chat")
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(16.dp, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .background(Color.White, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        tabs.forEachIndexed { index, tab ->
            BottomNavItem(
                iconRes = tab.iconRes,
                label = tab.label,
                isSelected = selectedTab == index,
                badgeCount = if (index == 2) cartItemCount else 0,
                hasNotification = index == 3,
                onClick = { onTabSelected(index) }
            )
        }
    }
}

@Composable
private fun BottomNavItem(
    iconRes: Int,
    label: String,
    isSelected: Boolean,
    badgeCount: Int = 0,
    hasNotification: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isSelected) BrandLightPink else Color.Transparent)
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(22.dp),
                    colorFilter = ColorFilter.tint(if (isSelected) BrandPrimary else TextGray)
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
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else if (hasNotification && isSelected) {
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
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    color = BrandPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }
    }
}
