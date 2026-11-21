package com.example.cursprojectmobile.presentation.home

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.model.FoodItem
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun ExploreMenuScreen(
    viewModel: HomeViewModel,
    onNavigateToFoodDetail: (Long) -> Unit,
    onNavigateToMealMenu: () -> Unit = {},
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
                    .height(200.dp)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Crop,
                alpha = 0.45f
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
            // 1. Header is part of the scrollable page (not pinned to top)
            item {
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
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White)
                            .shadow(2.dp, RoundedCornerShape(12.dp))
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
            }

            // 2. Search Bar directly below Header (matching Figma design)
            item {
                Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)) {
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

            // Search Results Mode
            if (uiState.searchQuery.isNotBlank()) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Search Results",
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

                items(uiState.filteredFoods) { food ->
                    PopularFoodSimpleCard(
                        food = food,
                        onClick = { onNavigateToFoodDetail(food.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            } else {
                // 3. Special Offer Banner (Ready-made graphic banner with click to burger detail)
                item {
                    val burgerId = uiState.popularFoods.firstOrNull { it.category == "Burger" }?.id
                        ?: uiState.filteredFoods.firstOrNull { it.category == "Burger" }?.id
                        ?: 1L

                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                onNavigateToFoodDetail(burgerId)
                            }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.food_banner_offer),
                            contentDescription = "Special Offer for March - Buy Now",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(985f / 450f),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                // 4. Category Chips Row (All, Burger, Pizza, Sandwich)
                item {
                    Spacer(modifier = Modifier.height(18.dp))
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

                // 5. Selected Category Title + Horizontal Menu
                item {
                    Spacer(modifier = Modifier.height(18.dp))
                    val categoryTitle = when {
                        uiState.selectedCategory.equals("Burger", ignoreCase = true) -> "Burgers"
                        uiState.selectedCategory.equals("Pizza", ignoreCase = true) -> "Pizzas"
                        uiState.selectedCategory.equals("Sandwich", ignoreCase = true) -> "Sandwiches & Meals"
                        else -> "Featured Menu"
                    }
                    Text(
                        text = categoryTitle,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(uiState.filteredFoods) { food ->
                            HorizontalFoodCard(
                                food = food,
                                onClick = { onNavigateToFoodDetail(food.id) },
                                onAddToCart = {
                                    viewModel.addToCart(food) {
                                        Toast.makeText(context, "${food.title} added to cart", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                    }
                }

                // 6. Section Header: Popular Meal Menu
                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Popular Meal Menu",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                        Text(
                            text = "See All >",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = BrandPrimary,
                            modifier = Modifier.clickable {
                                onNavigateToMealMenu()
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // 7. Popular Menu Items (Vertical list of simple cards like design screenshot)
                items(uiState.popularFoods) { food ->
                    PopularFoodSimpleCard(
                        food = food,
                        onClick = { onNavigateToFoodDetail(food.id) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}
}

/**
 * Horizontal food card for the selected category row
 */
@Composable
private fun HorizontalFoodCard(
    food: FoodItem,
    onClick: () -> Unit,
    onAddToCart: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val drawableId = FoodImages.getDrawableByName(food.imageResName)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(105.dp),
                contentAlignment = Alignment.Center
            ) {
                FoodImagePlaceholder(
                    drawableRes = drawableId,
                    emoji = when (food.category) {
                        "Pizza" -> "🍕"
                        "Sandwich" -> "🥪"
                        else -> "🍔"
                    },
                    cornerRadius = 14.dp,
                    modifier = Modifier.size(100.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = food.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = food.subtitle.ifEmpty { "${food.category} • Hot" },
                fontSize = 11.sp,
                color = TextGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "$${"%.0f".format(food.price)}",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = BrandPrimary
                )

                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(BrandPrimary)
                        .clickable { onAddToCart() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

/**
 * Simple horizontal row card for "Popular Meal Menu" (matching user's screenshot exactly)
 */
@Composable
private fun PopularFoodSimpleCard(
    food: FoodItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
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
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.subtitle.ifEmpty { food.description.take(24) },
                    fontSize = 13.sp,
                    color = Color(0xFFAAAAAA)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "$${"%.0f".format(food.price)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = BrandPrimary
            )
        }
    }
}
