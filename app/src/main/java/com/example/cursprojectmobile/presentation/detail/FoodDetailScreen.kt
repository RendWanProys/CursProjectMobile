package com.example.cursprojectmobile.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.presentation.components.FoodImagePlaceholder
import com.example.cursprojectmobile.presentation.components.FoodImages
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun FoodDetailScreen(
    foodId: Long,
    viewModel: DetailViewModel,
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val food = uiState.foodItem
    var isQuantityExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(foodId) {
        viewModel.loadFoodItem(foodId)
    }

    if (food == null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = BrandPrimary)
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFF2F4))
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar with Back chevron and Cart button
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
                    .clickable { onBack() },
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
                text = "Item Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark
            )

            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFFDE8EC))
                    .clickable { onNavigateToCart() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🛒", fontSize = 18.sp)
            }
        }

        // Hero Food Image in top pink section
        val imageRes = FoodImages.getDrawableByName(food.imageResName)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .clip(RoundedCornerShape(22.dp)),
            contentAlignment = Alignment.Center
        ) {
            FoodImagePlaceholder(
                drawableRes = imageRes,
                emoji = when (food.category) {
                    "Pizza" -> "🍕"
                    "Sandwich" -> "🥪"
                    else -> "🍔"
                },
                cornerRadius = 22.dp,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Bottom White Card Sheet with rounded top corners
        Surface(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White,
            shadowElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                // Little handle bar
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(44.dp)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Color(0xFFEADBDF))
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Popular Badge + Pin & Heart Icons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFFDE8EC))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Popular",
                            color = BrandPrimary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFDE8EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "📍", fontSize = 15.sp)
                        }

                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFFDE8EC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "❤️", fontSize = 15.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title and Price Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = food.title,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = TextDark
                        )
                        if (food.subtitle.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = food.subtitle,
                                fontSize = 13.sp,
                                fontFamily = Poppins,
                                color = TextGray
                            )
                        }
                    }

                    Text(
                        text = "$${"%.2f".format(food.price)}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = BrandPrimary
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Rating & Orders row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("⭐", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${food.rating} Rating",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Poppins,
                        color = TextDark
                    )

                    Spacer(modifier = Modifier.width(24.dp))

                    Text("🛍️", fontSize = 15.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${food.orderCount}+ Orders",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        fontFamily = Poppins,
                        color = TextDark
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Description",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = food.description.ifEmpty {
                        "Prepared fresh to order using premium culinary ingredients, expertly balanced sauces, and handcrafted seasoning for the ultimate dining experience."
                    },
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = TextGray,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }

        // Bottom Action Bar: Initially a single wide button, expanding into stepper + button
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 14.dp,
            color = Color.White
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp)
            ) {
                if (!isQuantityExpanded) {
                    // 1. Initial State: Single large "Add to Cart • $Price" button
                    Button(
                        onClick = {
                            isQuantityExpanded = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                    ) {
                        Text(
                            text = "Add to Cart • $${"%.2f".format(food.price)}",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }
                } else {
                    // 2. Expanded State: Stepper [- Qty +] on the left, Add to Cart on the right
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Quantity Stepper
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFFF3F3F3))
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(Color.White)
                                    .clickable {
                                        if (uiState.quantity > 1) {
                                            viewModel.decrementQuantity()
                                        } else {
                                            isQuantityExpanded = false
                                        }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "−",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextDark
                                )
                            }

                            Spacer(modifier = Modifier.width(14.dp))

                            Text(
                                text = "${uiState.quantity}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins,
                                color = TextDark
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(BrandPrimary)
                                    .clickable { viewModel.incrementQuantity() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        // Add to Cart Button with total price
                        Button(
                            onClick = {
                                viewModel.addToCart {
                                    Toast.makeText(
                                        context,
                                        "${food.title} x${uiState.quantity} added to cart!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onNavigateToCart()
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)
                                .height(50.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                        ) {
                            Text(
                                text = "Add to Cart • $${"%.2f".format(food.price * uiState.quantity)}",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Poppins
                            )
                        }
                    }
                }
            }
        }
    }
}
