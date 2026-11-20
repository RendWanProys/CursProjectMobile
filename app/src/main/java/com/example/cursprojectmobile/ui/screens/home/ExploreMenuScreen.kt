package com.example.cursprojectmobile.ui.screens.home

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.model.Category
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.FoodRepository
import com.example.cursprojectmobile.ui.components.FoodImagePlaceholder
import com.example.cursprojectmobile.ui.components.FoodImages

val PrimaryRed = Color(0xFFD61355)
val SoftPink = Color(0xFFFFF0F3)
val BorderPink = Color(0xFFFFD6E0)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF757575)

@Composable
fun ExploreMenuScreen(
    foodRepository: FoodRepository,
    cartRepository: CartRepository,
    currentAddress: String,
    onNavigateToFoodDetail: (Long) -> Unit,
    onNavigateToLocation: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onCartUpdated: () -> Unit
) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Burger") }

    val categories = remember { foodRepository.getCategories() }
    var foodList by remember { mutableStateOf(emptyList<FoodItem>()) }
    var popularFoods by remember { mutableStateOf(emptyList<FoodItem>()) }

    fun refreshData() {
        popularFoods = foodRepository.getPopularFoods()
        foodList = if (searchQuery.isNotBlank()) {
            foodRepository.searchFoods(searchQuery.trim())
        } else {
            foodRepository.getFoodsByCategory(selectedCategory)
        }
    }

    LaunchedEffect(selectedCategory, searchQuery) {
        refreshData()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Menu / Hamburger button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .clickable { /* Toggle drawer / menu */ },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "☰", fontSize = 20.sp, color = TextDark)
            }

            // Location Selector Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(SoftPink)
                    .clickable { onNavigateToLocation() }
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "📍", fontSize = 16.sp)
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentAddress.take(24) + if (currentAddress.length > 24) "..." else "",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryRed
                )
            }

            // User Avatar
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(SoftPink)
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

        // --- SCROLLABLE CONTENT ---
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Title & Search
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

                    // Search Bar
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(16.dp))
                            .background(Color.White, RoundedCornerShape(16.dp)),
                        placeholder = { Text("Search your favorite food...", color = Color(0xFFA0A0A0), fontSize = 14.sp) },
                        leadingIcon = { Text("🔍", fontSize = 18.sp) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                Text(
                                    text = "✕",
                                    fontSize = 16.sp,
                                    color = TextGray,
                                    modifier = Modifier.clickable { searchQuery = "" }
                                )
                            }
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryRed,
                            unfocusedBorderColor = Color(0xFFF0F0F0)
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { refreshData() })
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            // Promo Banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(145.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFFF3366), Color(0xFFD61355))
                            )
                        )
                ) {
                    FoodImagePlaceholder(
                        drawableRes = FoodImages.BANNER_OFFER,
                        emoji = "🍔🍕",
                        cornerRadius = 20.dp,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Overlay text & button
                    Column(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(220.dp)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Special Offer\nfor March",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 22.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Best desserts & burgers in town",
                                color = Color(0xFFFFD6E0),
                                fontSize = 11.sp
                            )
                        }

                        Button(
                            onClick = {
                                val burger = popularFoods.firstOrNull()
                                if (burger != null) onNavigateToFoodDetail(burger.id)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
                        ) {
                            Text("Buy Now", color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            // Categories
            item {
                Column {
                    Text(
                        text = "Categories",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(categories) { cat ->
                            val isSelected = selectedCategory == cat.name
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) PrimaryRed else Color.White)
                                    .border(
                                        width = 1.dp,
                                        color = if (isSelected) PrimaryRed else Color(0xFFE8E8E8),
                                        shape = RoundedCornerShape(20.dp)
                                    )
                                    .clickable { selectedCategory = cat.name }
                                    .padding(horizontal = 16.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = cat.iconEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = cat.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color.White else TextDark
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // Popular Items Grid / List
            item {
                Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Choices",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "${foodList.size} items",
                            fontSize = 12.sp,
                            color = TextGray
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            // Food Items Cards
            items(foodList) { food ->
                FoodCardRow(
                    food = food,
                    onItemClick = { onNavigateToFoodDetail(food.id) },
                    onAddToCart = {
                        cartRepository.addToCart(food.id, 1)
                        onCartUpdated()
                        Toast.makeText(context, "${food.title} added to cart!", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun FoodCardRow(
    food: FoodItem,
    onItemClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable(onClick = onItemClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image / Placeholder
            FoodImagePlaceholder(
                drawableRes = FoodImages.getDrawableByName(food.imageResName),
                emoji = if (food.category == "Pizza") "🍕" else if (food.category == "Burger") "🍔" else "🥪",
                cornerRadius = 14.dp,
                modifier = Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Food Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.subtitle,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "⭐ ${food.rating}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFF59E0B)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "• ${food.orderCount}+ sold",
                        fontSize = 11.sp,
                        color = TextGray
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Price & Add Button
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "$ ${"%.2f".format(food.price)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = PrimaryRed
                )
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                        .clickable(onClick = onAddToCart),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
