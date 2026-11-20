package com.example.cursprojectmobile.presentation.chat

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.domain.model.ChatMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            id = 1,
            sender = "Support",
            text = "Hello! 👋 Welcome to Food Couriers support. How can we help you today?",
            isFromUser = false,
            timestamp = "10:00 AM"
        ),
        ChatMessage(
            id = 2,
            sender = "Support",
            text = "You can ask about your order status, delivery times, or discount coupons!",
            isFromUser = false,
            timestamp = "10:01 AM"
        )
    ),
    val inputText: String = ""
)

class ChatViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    fun onInputTextChange(text: String) {
        _uiState.update { it.copy(inputText = text) }
    }

    fun sendMessage() {
        val text = _uiState.value.inputText.trim()
        if (text.isEmpty()) return

        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
        val userMsg = ChatMessage(
            id = System.currentTimeMillis(),
            sender = "Me",
            text = text,
            isFromUser = true,
            timestamp = now
        )

        val updatedList = _uiState.value.messages.toMutableList()
        updatedList.add(userMsg)

        // Generate instant smart bot answer
        val replyText = generateBotReply(text)
        val botMsg = ChatMessage(
            id = System.currentTimeMillis() + 1,
            sender = "Support",
            text = replyText,
            isFromUser = false,
            timestamp = now
        )
        updatedList.add(botMsg)

        _uiState.update { it.copy(messages = updatedList, inputText = "") }
    }

    private fun generateBotReply(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("order") || q.contains("status") || q.contains("where") ->
                "Your latest order is currently in the kitchen and will be handed over to your courier shortly! Average delivery time is 25-35 minutes."
            q.contains("discount") || q.contains("promo") || q.contains("coupon") ->
                "Use promo code 'FOOD2026' at checkout to receive free delivery on orders above $25!"
            q.contains("burger") || q.contains("menu") || q.contains("pizza") ->
                "Check out our Popular Menu tab on the home screen — our Chicken Burger and Pepper Pizza are customer favorites today!"
            q.contains("hello") || q.contains("hi") || q.contains("hey") ->
                "Hello there! Ready for some delicious food? Let me know if you need any recommendations."
            else ->
                "Thank you for contacting us! Our support team is online 24/7. Your inquiry has been noted and we're here to assist you."
        }
    }
}
