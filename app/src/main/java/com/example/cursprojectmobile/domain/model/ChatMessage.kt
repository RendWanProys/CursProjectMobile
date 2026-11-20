package com.example.cursprojectmobile.domain.model

data class ChatMessage(
    val id: Long = System.currentTimeMillis(),
    val sender: String = "Support",
    val text: String = "",
    val isFromUser: Boolean = false,
    val timestamp: String = ""
)
