package com.example.cursprojectmobile.presentation.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursprojectmobile.domain.model.ChatMessage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ChatUiState(
    val messages: List<ChatMessage> = listOf(
        ChatMessage(
            id = 1,
            sender = "Support",
            text = "Hello! my name is Food Couriers\nbefore we start, what is your name?",
            isFromUser = false,
            timestamp = "10:00 AM"
        )
    ),
    val inputText: String = "",
    val isBotTyping: Boolean = false
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

        _uiState.update {
            it.copy(
                messages = updatedList,
                inputText = "",
                isBotTyping = true
            )
        }

        viewModelScope.launch {
            delay(750)
            val replyText = generateBotReply(text)
            val botMsg = ChatMessage(
                id = System.currentTimeMillis() + 1,
                sender = "Support",
                text = replyText,
                isFromUser = false,
                timestamp = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            )
            val finalList = _uiState.value.messages.toMutableList()
            finalList.add(botMsg)
            _uiState.update {
                it.copy(
                    messages = finalList,
                    isBotTyping = false
                )
            }
        }
    }

    private fun generateBotReply(query: String): String {
        val q = query.lowercase()
        return when {
            q.contains("order") || q.contains("status") || q.contains("where") ->
                "Your latest order is currently in the kitchen and will be handed over to your courier shortly! Average delivery time is 25-35 minutes."
            q.contains("discount") || q.contains("promo") || q.contains("coupon") ->
                "Use promo code 'FOOD2026' at checkout to receive free delivery on orders above $25!"
            q.contains("burger") || q.contains("menu") || q.contains("pizza") || q.contains("rice") ->
                "Check out our Popular Menu tab on the home screen — our Chicken Burger and Pepper Pizza are customer favorites today!"
            q.contains("hello") || q.contains("hi") || q.contains("hey") ->
                "Hello there! Ready for some delicious food? Let me know if you need any recommendations."
            q.contains("emmanuel") || q.contains("kamcy") || q.contains("name is") || q.contains("i am") || q.contains("my name") ->
                "Hello! Emmanuel 👋. I can converse in your preferred language. How may I help you today?"
            else ->
                "Thank you for contacting Food Couriers! Our support team is online 24/7. Your inquiry has been noted and we're here to assist you."
        }
    }
}
