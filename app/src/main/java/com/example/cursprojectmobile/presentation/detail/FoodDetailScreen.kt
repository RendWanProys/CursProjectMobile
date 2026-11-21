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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import Rg.CursProjectMobile.R
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
            .background(Color.White)
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        // Top Bar: Back circle button, Title, Cart circle button (matching Image 6)
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
                    .clip(CircleShape)
                    .background(Color(0xFFF7F8F9))
                    .clickable { onBack() },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "\u2190",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
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
                    .clip(CircleShape)
                    .background(Color(0xFFF7F8F9))
                    .clickable { onNavigateToCart() },
                contentAlignment = Alignment.Center
            ) {
                Text(text = "\uD83D\uDED2", fontSize = 18.sp)
            }
        }

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            // Food Hero Image with rounded corners matching Image 6
            val imageRes = FoodImages.getDrawableByName(food.imageResName)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .clip(RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                FoodImagePlaceholder(
                    drawableRes = imageRes,
                    emoji = when (food.category) {
                        "Pizza" -> "\uD83C\uDF55"
                        "Sandwich" -> "\uD83E\uDD6A"
                        else -> "\uD83C\uDF54"
                    },
                    cornerRadius = 20.dp,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Title and Price Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = food.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "$${String.format(Locale.US, "%.2f", food.price)}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = BrandPrimary
                )
            }

            // Subtitle
            if (food.subtitle.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = food.subtitle,
                    fontSize = 13.sp,
                    fontFamily = Poppins,
                    color = TextGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Stats Pill Row (Rating, Orders, Category) matching Image 6
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\u2B50", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${food.rating} Rating",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = TextDark
                        )
                    }

                    // Orders
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83D\uDD25", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${food.orderCount}+ Orders",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = TextDark
                        )
                    }

                    // Category / Icon
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("\uD83C\uDF7D\uFE0F", fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = food.category,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins,
                            color = TextDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Description Heading
            Text(
                text = "Description",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = Poppins,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description Body
            Text(
                text = food.description.ifEmpty {
                    "Tender grilled chicken fillet seasoned with fragrant spices, sliced tomatoes, melted gouda and fresh crispy iceberg lettuce."
                },
                fontSize = 14.sp,
                fontFamily = Poppins,
                color = TextGray,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Bottom Action Bar: Counter on left, Add to Cart on right (Matching Image 6)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shadowElevation = 8.dp,
            color = Color.White
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Quantity Stepper Pill [- Qty +]
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFFF5F6F8))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { viewModel.decrementQuantity() },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "–",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextDark
                        )
                    }

                    Text(
                        text = "${uiState.quantity}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(BrandPrimary)
                            .clickable { viewModel.incrementQuantity() },
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

                // Wide "Add to Cart • $Price" Button
                val totalPrice = food.price * uiState.quantity
                Button(
                    onClick = {
                        viewModel.addToCart {
                            Toast.makeText(context, "${food.title} added to cart", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrandPrimary)
                ) {
                    Text(
                        text = "Add to Cart • $${String.format(Locale.US, "%.2f", totalPrice)}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins
                    )
                }
            }
        }
    }
}
