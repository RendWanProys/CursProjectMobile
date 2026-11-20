package com.example.cursprojectmobile.ui.screens.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

val PrimaryRed = Color(0xFFD61355)
val SoftPink = Color(0xFFFFF0F3)
val TextDark = Color(0xFF1F1F1F)
val TextGray = Color(0xFF757575)

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val text: String,
    val isFromUser: Boolean = false
)

@Composable
fun ChatScreen(
    onNavigateBack: () -> Unit
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    text = "Hello! My name is Food Couriers bot. Before we start, how may I help you today?",
                    isFromUser = false
                ),
                ChatMessage(
                    text = "Hello! 👋 I can answer your questions about menu, delivery time or order status.",
                    isFromUser = false
                )
            )
        )
    }

    var inputText by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    fun sendMessage() {
        if (inputText.isBlank()) return
        val userMsg = ChatMessage(text = inputText.trim(), isFromUser = true)
        val currentInput = inputText.trim().lowercase()
        messages = messages + userMsg
        inputText = ""

        coroutineScope.launch {
            listState.animateScrollToItem(messages.size - 1)
            delay(800)
            val botReply = when {
                "burger" in currentInput -> "We have delicious Chicken & Cheese burgers starting at $15! Check out the menu tab."
                "pizza" in currentInput -> "Our Pepper and Onion pizzas are freshly baked! Average delivery time is 25-30 minutes."
                "delivery" in currentInput || "time" in currentInput -> "Standard delivery takes about 20-35 minutes depending on your location."
                "order" in currentInput -> "You can track your orders in the Profile tab under 'Order History'."
                else -> "Got it! Our support team is always here to assist your food delivery experience! 🛵🍕"
            }
            messages = messages + ChatMessage(text = botReply, isFromUser = false)
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // --- HEADER ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
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

            Text(
                text = "Chat Support",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPink),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🔔", fontSize = 18.sp)
            }
        }

        // --- MESSAGES LIST ---
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(messages) { msg ->
                if (msg.isFromUser) {
                    // User Message (Right side, Red bubble)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .widthIn(max = 270.dp)
                                .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 4.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
                                .background(PrimaryRed)
                                .padding(14.dp)
                        ) {
                            Text(text = msg.text, color = Color.White, fontSize = 14.sp)
                        }
                    }
                } else {
                    // Bot Message (Left side, Grey bubble with robot icon)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(PrimaryRed),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 18.sp)
                        }

                        Spacer(modifier = Modifier.width(10.dp))

                        Box(
                            modifier = Modifier
                                .widthIn(max = 260.dp)
                                .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
                                .background(Color(0xFFF6F6F6))
                                .padding(14.dp)
                        ) {
                            Text(text = msg.text, color = TextDark, fontSize = 14.sp, lineHeight = 20.sp)
                        }
                    }
                }
            }
        }

        // --- BOTTOM INPUT BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(SoftPink)
                    .clickable { /* attach photo / order */ },
                contentAlignment = Alignment.Center
            ) {
                Text("📎", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                placeholder = { Text("Ask anything...", color = Color(0xFFAAAAAA), fontSize = 14.sp) },
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryRed,
                    unfocusedBorderColor = Color(0xFFE8E8E8)
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PrimaryRed)
                    .clickable(onClick = { sendMessage() }),
                contentAlignment = Alignment.Center
            ) {
                Text("➤", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
