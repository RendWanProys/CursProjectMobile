package com.example.cursprojectmobile.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.model.Category
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandBorderPink
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.BrandSecondary
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun ExploreMenuScreen(
    viewModel: HomeViewModel,
    onNavigateToFoodDetail: (Long) -> Unit,
    onNavigateToLocation: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToChat: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refreshSessionAddress()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = 0,
                cartItemCount = uiState.cartCount,
                onTabSelected = { tabIndex ->
                    when (tabIndex) {
                        0 -> {} // Already on Home
                        1 -> onNavigateToProfile()
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
            // Header Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .clickable { },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "☰", fontSize = 20.sp, color = TextDark)
                }

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(BrandLightPink)
                        .clickable { onNavigateToLocation() }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📍", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = uiState.deliveryAddress.take(22) + if (uiState.deliveryAddress.length > 22) "..." else "",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = BrandPrimary
                    )
                }

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(BrandLightPink)
                        .clickable { onNavigateToProfile() },
                    contentAlignment = Alignment.Center
                ) {
                    FoodImagePlaceholder(
                        drawableRes = FoodImages.USER_AVATAR,
                        emoji = "👩",
                        cornerRadius = 22.dp,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                // Title and Search
                item {
                    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                        Text(
                            text = "Delicious food\nReady to deliver",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextDark,
                            lineHeight = 30.sp
                        )
                        Spacer(modifier = Modifier.height(14.dp))

                        OutlinedTextField(
                            value = uiState.searchQuery,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .shadow(3.dp, RoundedCornerShape(16.dp))
                                .background(Color.White, RoundedCornerShape(16.dp)),
                            placeholder = { Text("Search your favorite food...", color = Color(0xFFA0A0A0), fontSize = 14.sp) },
                            leadingIcon = { Text("🔍", fontSize = 18.sp) },
                            trailingIcon = {
                                if (uiState.searchQuery.isNotEmpty()) {
                                    Text(
                                        text = "✕",
                                        fontSize = 16.sp,
                                        color = TextGray,
                                        modifier = Modifier.clickable { viewModel.onSearchQueryChange("") }
                                    )
                                }
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = BrandPrimary,
                                unfocusedBorderColor = Color(0xFFF0F0F0)
                            ),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { viewModel.onSearchQueryChange(uiState.searchQuery) })
                        )
                    }
                }

                // Special Offer Banner
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(140.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFD61355),
                                        Color(0xFFFF5277)
                                    )
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth(0.65f)) {
                            Text(
                                text = "Special Deal for Today",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Free Delivery\nOn 1st Order!",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 24.sp
                            )
                        }

                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(90.dp)
                        ) {
                            FoodImagePlaceholder(
                                drawableRes = FoodImages.BANNER_OFFER,
                                emoji = "🎁",
                                modifier = Modifier.fillMaxSize(),
                                cornerRadius = 16.dp
                            )
                        }
                    }
                }

                // Category Chips
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(uiState.categories) { category ->
                            val isSelected = uiState.selectedCategory.equals(category.name, ignoreCase = true)
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) BrandPrimary else Color.White)
                                    .border(1.dp, if (isSelected) BrandPrimary else Color(0xFFEBEBEB), RoundedCornerShape(20.dp))
                                    .clickable { viewModel.selectCategory(category.name) }
                                    .padding(horizontal = 16.dp, vertical = 10.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = category.iconEmoji, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = category.name,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else TextDark
                                    )
                                }
                            }
                        }
                    }
                }

                // Section Title: Popular or Filtered Menu
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (uiState.searchQuery.isNotBlank()) "Search Results" else "Popular Menu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "${uiState.filteredFoods.size} items",
                            fontSize = 13.sp,
                            color = TextGray
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Food Items List
                items(uiState.filteredFoods) { food ->
                    FoodListItemCard(
                        food = food,
                        onClick = { onNavigateToFoodDetail(food.id) },
                        onAddToCart = {
                            viewModel.addToCart(food) {
                                Toast.makeText(context, "${food.title} added to cart", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun FoodListItemCard(
    food: FoodItem,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val drawableId = FoodImages.getDrawableByName(food.imageResName)
            FoodImagePlaceholder(
                drawableRes = drawableId,
                emoji = when (food.category) {
                    "Pizza" -> "🍕"
                    "Sandwich" -> "🥪"
                    else -> "🍔"
                },
                cornerRadius = 14.dp,
                modifier = Modifier.size(76.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                if (food.subtitle.isNotEmpty()) {
                    Text(
                        text = food.subtitle,
                        fontSize = 12.sp,
                        color = TextGray
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${food.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "$${"%.2f".format(food.price)}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = BrandPrimary
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(BrandPrimary)
                    .clickable { onAddToCart() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "+", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
