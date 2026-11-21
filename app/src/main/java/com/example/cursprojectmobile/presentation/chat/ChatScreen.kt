package com.example.cursprojectmobile.presentation.chat

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.cursprojectmobile.domain.model.ChatMessage
import com.example.cursprojectmobile.presentation.components.BottomNavBar
import com.example.cursprojectmobile.ui.theme.BrandLightPink
import com.example.cursprojectmobile.ui.theme.BrandPrimary
import com.example.cursprojectmobile.ui.theme.Poppins
import com.example.cursprojectmobile.ui.theme.TextDark
import com.example.cursprojectmobile.ui.theme.TextGray

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    cartItemCount: Int,
    onNavigateToHome: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive or bot is typing
    LaunchedEffect(uiState.messages.size, uiState.isBotTyping) {
        val totalCount = uiState.messages.size + if (uiState.isBotTyping) 1 else 0
        if (totalCount > 0) {
            listState.animateScrollToItem(totalCount - 1)
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                selectedTab = 3,
                cartItemCount = cartItemCount,
                onTabSelected = { tab ->
                    when (tab) {
                        0 -> onNavigateToHome()
                        1 -> onNavigateToProfile()
                        2 -> onNavigateToCart()
                        3 -> {} // Already on Chat
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
                .imePadding()
        ) {
            // Food doodle pattern in top-right background
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

                // Top Bar with Back, Title, Notification Bell
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
                            .clickable { onNavigateToHome() },
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
                        text = "Chat",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Poppins,
                        color = TextDark
                    )

                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFDE8EC)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "🔔", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Chat Messages LazyColumn
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.messages, key = { it.id }) { message ->
                        ChatMessageItem(message = message)
                    }

                    if (uiState.isBotTyping) {
                        item {
                            BotTypingBubble()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Messenger Input Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Attachment Paperclip Button
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFFFDE8EC))
                            .clickable { },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "📎", fontSize = 20.sp)
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Text Input Bar with Send button
                    Surface(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        shape = RoundedCornerShape(25.dp),
                        color = Color(0xFFF6F6F6)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedTextField(
                                value = uiState.inputText,
                                onValueChange = { viewModel.onInputTextChange(it) },
                                placeholder = {
                                    Text(
                                        text = "Type a message...",
                                        color = Color(0xFF9E9E9E),
                                        fontSize = 14.sp,
                                        fontFamily = Poppins
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent,
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent
                                ),
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                                keyboardActions = KeyboardActions(onSend = { viewModel.sendMessage() })
                            )

                            IconButton(
                                onClick = { viewModel.sendMessage() },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text(
                                    text = "➤",
                                    color = BrandPrimary,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChatMessageItem(message: ChatMessage) {
    val isUser = message.isFromUser
    if (isUser) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Box(
                modifier = Modifier
                    .widthIn(max = 280.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 18.dp,
                            topEnd = 4.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .background(BrandPrimary)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    lineHeight = 20.sp
                )
            }
        }
    } else {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BrandPrimary),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🤖", fontSize = 20.sp)
            }

            Spacer(modifier = Modifier.width(10.dp))

            Box(
                modifier = Modifier
                    .widthIn(max = 270.dp)
                    .clip(
                        RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 18.dp,
                            bottomStart = 18.dp,
                            bottomEnd = 18.dp
                        )
                    )
                    .background(Color(0xFFF6F6F6))
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = message.text,
                    color = TextDark,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    lineHeight = 20.sp
                )
            }
        }
    }
}

@Composable
private fun BotTypingBubble() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandPrimary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "🤖", fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(10.dp))

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFF6F6F6))
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            Text(
                text = "••••",
                color = BrandPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
