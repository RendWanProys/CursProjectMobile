package com.example.cursprojectmobile.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.data.repository.FoodRepository
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun MealMenuScreen(
    viewModel: HomeViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToFoodDetail: (Long) -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigateToChat: () -> Unit,
    onNavigateToNotifications: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    val foodRepo = remember { FoodRepository() }
    val allFoods = remember { foodRepo.getAllFoods() }

    val displayedFoods = remember(searchQuery, allFoods) {
        if (searchQuery.trim().isEmpty()) {
            allFoods
        } else {
            allFoods.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                it.subtitle.contains(searchQuery, ignoreCase = true) ||
                it.category.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = 0,
                cartItemCount = uiState.cartCount,
                onTabSelected = { tabIndex ->
                    when (tabIndex) {
                        0 -> onNavigateBack()
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
            // Doodle pattern at the top
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Header Row (Back, Meal Menu Title, Notification)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFDE8EC))
                            .clickable { onNavigateBack() },
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
                        text = "Meal Menu",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFDE8EC))
                            .clickable { onNavigateToNotifications() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "\uD83D\uDD14",
                            fontSize = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Search Bar (Light pink rounded pill matching mockup)
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = {
                        Text(
                            text = "Search your favorite food...",
                            color = Color(0xFF9E9E9E),
                            fontSize = 14.sp,
                            fontFamily = Poppins
                        )
                    },
                    leadingIcon = {
                        Text(text = "🔍", fontSize = 16.sp)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFFFDE8EC),
                        unfocusedContainerColor = Color(0xFFFDE8EC),
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent
                    ),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List of Meal Items
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                    items(displayedFoods, key = { it.id }) { food ->
                        MealMenuCard(
                            food = food,
                            onClick = { onNavigateToFoodDetail(food.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun MealMenuCard(
    food: FoodItem,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
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
            // Food image thumbnail
            val imageRes = FoodImages.getDrawableByName(food.imageResName)
            FoodImagePlaceholder(
                drawableRes = imageRes,
                emoji = when (food.category) {
                    "Pizza" -> "🍕"
                    "Sandwich" -> "🥪"
                    else -> "🍔"
                },
                cornerRadius = 14.dp,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Title & Subtitle
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = food.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.subtitle.ifEmpty { "${food.category} Special" },
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    color = TextGray
                )
            }

            // Price in bold BrandPrimary
            Text(
                text = "$${food.price.toInt()}",
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold,
                fontFamily = Poppins,
                color = BrandPrimary
            )
        }
    }
}
