package com.example.cursprojectmobile.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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

private val SoftPinkBadge = Color(0xFFFDE8EC)
private val LightGrayBadge = Color(0xFFF0F0F2)
private val MetaGrayText = Color(0xFF8C8E98)
private val StarGold = Color(0xFFFFB800)

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
    var isFavorite by remember { mutableStateOf(false) }

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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- 1. SCROLLABLE CONTENT ---
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 90.dp) // Room for bottom action bar
        ) {
            // Full bleed hero image (edge-to-edge, reaching the very top edge)
            val imageRes = FoodImages.getDrawableByName(food.imageResName) ?: FoodImages.BURGER_DETAIL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(310.dp)
            ) {
                FoodImagePlaceholder(
                    drawableRes = imageRes,
                    emoji = when (food.category) {
                        "Pizza" -> "🍕"
                        "Sandwich" -> "🥪"
                        else -> "🍔"
                    },
                    cornerRadius = 0.dp,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // --- 2. BOTTOM DETAILS SHEET (Rounded top corners overlapping the image) ---
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-24).dp)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 22.dp)
            ) {
                // Top Row: [Popular] badge on left, [📍] and [🖤] on right
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(10.dp))
                            .background(SoftPinkBadge)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = if (food.isPopular) "Popular" else food.category,
                            color = BrandPrimary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = Poppins
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Location pin icon button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(SoftPinkBadge)
                                .clickable {
                                    Toast.makeText(context, "Location tagged", Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = "Location",
                                tint = BrandPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        // Favorite heart icon button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(if (isFavorite) SoftPinkBadge else LightGrayBadge)
                                .clickable {
                                    isFavorite = !isFavorite
                                    val msg = if (isFavorite) "Added to favorites" else "Removed from favorites"
                                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = "Favorite",
                                tint = if (isFavorite) BrandPrimary else Color(0xFF1E1E2E),
                                modifier = Modifier.size(19.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Title (No price on this line, matching target design)
                Text(
                    text = food.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Poppins,
                    color = TextDark,
                    lineHeight = 32.sp
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Rating & Orders Meta Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(22.dp)
                ) {
                    // Star Rating
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.Star,
                            contentDescription = "Rating",
                            tint = StarGold,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${food.rating.toString().replace('.', ',')} Rating",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Poppins,
                            color = MetaGrayText
                        )
                    }

                    // Orders Count
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.ShoppingBag,
                            contentDescription = "Orders",
                            tint = BrandPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${food.orderCount}+ Order",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Poppins,
                            color = MetaGrayText
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Body Description text
                Text(
                    text = food.description.ifEmpty {
                        "In a medium bowl, add ground chicken, breadcrumbs, mayonnaise, onions, parsley, garlic, paprika, salt and pepper. Use your hands to combine all the ingredients together until blended, but don't over mix."
                    },
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    color = Color(0xFF333333),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // --- 3. FLOATING TOP OVERLAY BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Top Left: Rounded-square pink back button with red chevron '<'
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

            // Top Right: Floating Cart Button
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.94f))
                    .clickable { onNavigateToCart() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Cart",
                    tint = TextDark,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // --- 4. BOTTOM ACTION BAR (Fixed at bottom) ---
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding(),
            shadowElevation = 10.dp,
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
