package com.example.cursprojectmobile.presentation.cart

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import Rg.CursProjectMobile.R
import com.example.cursprojectmobile.data.model.CartItem
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToChat: () -> Unit,
    onOrderPlaced: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadCart()
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = 2,
                cartItemCount = uiState.cartItems.sumOf { it.quantity },
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToProfile()
                        2 -> {} // Already on Cart
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
            // Food doodle pattern in top right background
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // Top Bar with Back Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                }

                Spacer(modifier = Modifier.height(16.dp))

                // "Order details" header (bold Poppins, left aligned)
                Text(
                    text = "Order details",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (uiState.cartItems.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "🛒", fontSize = 64.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Your Cart is Empty",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins,
                                color = TextDark
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Looks like you haven't added anything to your cart yet",
                                fontSize = 14.sp,
                                fontFamily = Poppins,
                                color = TextGray
                            )
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = { onNavigateToHome() },
                                colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "Explore Menu",
                                    color = Color.White,
                                    fontFamily = Poppins
                                )
                            }
                        }
                    }
                } else {
                    // List of items
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(uiState.cartItems, key = { it.id }) { item ->
                            CartItemRow(
                                item = item,
                                onIncrement = { viewModel.incrementItem(item.id) },
                                onDecrement = { viewModel.decrementItem(item.id) },
                                onDelete = { viewModel.removeItem(item.id) }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bottom Red Summary Card matching Mockup 1.31
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(22.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(BrandPrimary, Color(0xFFFF2E63))
                                )
                            )
                            .shadow(8.dp, RoundedCornerShape(22.dp))
                    ) {
                        // Food doodle overlay inside the red card
                        Image(
                            painter = painterResource(id = R.drawable.bg_food_pattern),
                            contentDescription = null,
                            modifier = Modifier.matchParentSize(),
                            contentScale = ContentScale.Crop,
                            alpha = 0.16f,
                            colorFilter = ColorFilter.tint(Color.White)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Sub-Total",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Poppins
                                )
                                Text(
                                    text = "${uiState.subtotal.toInt()} $",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = Poppins
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Delivery Charge",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Poppins
                                )
                                Text(
                                    text = "${uiState.deliveryFee.toInt()} $",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = Poppins
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Discount",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Medium,
                                    fontFamily = Poppins
                                )
                                Text(
                                    text = "${uiState.discount.toInt()} $",
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    fontFamily = Poppins
                                )
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Total",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Poppins
                                )
                                Text(
                                    text = "${uiState.total.toInt()}$",
                                    color = Color.White,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = Poppins
                                )
                            }

                            Spacer(modifier = Modifier.height(18.dp))

                            // White "Place My Order" button
                            Button(
                                onClick = {
                                    viewModel.placeOrder { orderId ->
                                        Toast.makeText(
                                            context,
                                            "Order placed successfully!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onOrderPlaced(orderId)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(52.dp),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                            ) {
                                Text(
                                    text = "Place My Order",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = Poppins,
                                    color = BrandPrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                }
            }
        }
    }
}

@Composable
private fun CartItemRow(
    item: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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
            val food = item.foodItem
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

            Spacer(modifier = Modifier.width(14.dp))

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
                    text = food.subtitle.ifEmpty { "Burger Factory LTD" },
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$ ${food.price.toInt()}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = BrandPrimary
                )
            }

            // Stepper with Minus [-] Qty [+]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFDE8EC))
                        .clickable { onDecrement() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "−",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrandPrimary
                    )
                }

                Text(
                    text = "${item.quantity}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(BrandPrimary)
                        .clickable { onIncrement() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "+",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}
