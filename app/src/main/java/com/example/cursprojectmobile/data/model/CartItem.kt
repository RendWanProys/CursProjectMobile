package com.example.cursprojectmobile.data.model

data class CartItem(
    val id: Long = 0,
    val foodItem: FoodItem,
    val quantity: Int = 1,
    val addedAt: Long = System.currentTimeMillis()
) {
    val totalPrice: Double
        get() = foodItem.price * quantity
}
