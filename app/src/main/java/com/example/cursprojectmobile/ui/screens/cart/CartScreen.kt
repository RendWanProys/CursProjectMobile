package com.example.cursprojectmobile.ui.screens.cart

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cursprojectmobile.data.model.CartItem
import com.example.cursprojectmobile.data.model.Order
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.OrderRepository
import com.example.cursprojectmobile.ui.components.FoodImagePlaceholder
import com.example.cursprojectmobile.ui.components.FoodImages

val PrimaryRed = Color(0xFFD61355)
val SoftPink = Color(0xFFFFF0F3)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF757575)

@Composable
fun CartScreen(
    cartRepository: CartRepository,
    orderRepository: OrderRepository,
    currentAddress: String,
    onNavigateBack: () -> Unit,
    onNavigateToHome: () -> Unit,
    onOrderPlaced: (Long) -> Unit,
    onCartUpdated: () -> Unit
) {
    val context = LocalContext.current
    var cartItems by remember { mutableStateOf(emptyList<CartItem>()) }

    fun refreshCart() {
        cartItems = cartRepository.getCartItems()
        onCartUpdated()
    }

    LaunchedEffect(Unit) {
        refreshCart()
    }

    val subtotal = cartItems.sumOf { it.totalPrice }
    val deliveryFee = if (cartItems.isNotEmpty()) 10.0 else 0.0
    val discount = if (cartItems.isNotEmpty()) 10.0 else 0.0
    val totalAmount = maxOf(0.0, subtotal + deliveryFee - discount)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPink)
                    .clickable(onClick = onNavigateBack),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "❮", fontSize = 18.sp, color = PrimaryRed, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Order details",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
        }

        if (cartItems.isEmpty()) {
            // Empty State
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "🛒", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Your cart is empty",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Explore our menu and add your favorite dishes!",
                        fontSize = 14.sp,
                        color = TextGray
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = onNavigateToHome,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryRed),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Text("Go to Menu", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            // --- CART ITEMS LIST ---
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                contentPadding = PaddingValues(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(cartItems) { item ->
                    CartItemCard(
                        cartItem = item,
                        onIncrement = {
                            cartRepository.updateQuantity(item.id, item.quantity + 1)
                            refreshCart()
                        },
                        onDecrement = {
                            cartRepository.updateQuantity(item.id, item.quantity - 1)
                            refreshCart()
                        }
                    )
                }
            }

            // --- BOTTOM SUMMARY & CHECKOUT CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .shadow(12.dp, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryRed)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFFE91E63), Color(0xFFD61355))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Sub-Total", color = Color(0xFFFFD6E0), fontSize = 14.sp)
                        Text("$ ${"%.2f".format(subtotal)}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Delivery Charge", color = Color(0xFFFFD6E0), fontSize = 14.sp)
                        Text("$ ${"%.2f".format(deliveryFee)}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Discount", color = Color(0xFFFFD6E0), fontSize = 14.sp)
                        Text("$ ${"%.2f".format(discount)}", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                    }

                    HorizontalDivider(
                        color = Color(0x40FFFFFF),
                        thickness = 1.dp,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "$ ${"%.2f".format(totalAmount)}",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            val newOrder = Order(
                                subtotal = subtotal,
                                deliveryFee = deliveryFee,
                                discount = discount,
                                totalAmount = totalAmount,
                                address = currentAddress
                            )
                            val orderId = orderRepository.createOrder(newOrder)
                            cartRepository.clearCart()
                            refreshCart()
                            Toast.makeText(context, "Order placed successfully!", Toast.LENGTH_SHORT).show()
                            onOrderPlaced(orderId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                    ) {
                        Text(
                            text = "Place My Order",
                            color = PrimaryRed,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Food Image Placeholder
            FoodImagePlaceholder(
                drawableRes = FoodImages.getDrawableByName(cartItem.foodItem.imageResName),
                emoji = if (cartItem.foodItem.category == "Pizza") "🍕" else "🍔",
                cornerRadius = 14.dp,
                modifier = Modifier.size(70.dp)
            )

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.foodItem.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = cartItem.foodItem.subtitle.ifBlank { cartItem.foodItem.category },
                    fontSize = 12.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$ ${"%.2f".format(cartItem.foodItem.price)}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryRed
                )
            }

            // Controls (- / +)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(SoftPink)
                        .clickable(onClick = onDecrement),
                    contentAlignment = Alignment.Center
                ) {
                    Text("-", color = PrimaryRed, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                Text(
                    text = cartItem.quantity.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark
                )

                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(PrimaryRed)
                        .clickable(onClick = onIncrement),
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
