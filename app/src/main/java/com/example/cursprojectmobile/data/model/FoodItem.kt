package com.example.cursprojectmobile.data.model

data class FoodItem(
    val id: Long = 0,
    val title: String,
    val subtitle: String = "",
    val description: String = "",
    val price: Double,
    val rating: Double = 4.5,
    val orderCount: Int = 1000,
    val category: String = "Burger",
    val imageResName: String = "",
    val isPopular: Boolean = false
)
