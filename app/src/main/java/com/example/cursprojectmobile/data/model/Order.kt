package com.example.cursprojectmobile.data.model

data class Order(
    val id: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val subtotal: Double,
    val deliveryFee: Double = 10.0,
    val discount: Double = 10.0,
    val totalAmount: Double,
    val address: String = "109, Freedom Way, Off Lekki Phase Victoria Island, Lagos.",
    val status: String = "Completed",
    val rating: Int = 0,
    val feedback: String = ""
)
