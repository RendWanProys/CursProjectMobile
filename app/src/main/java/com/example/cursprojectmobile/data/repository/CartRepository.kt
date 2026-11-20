package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.FoodCouriersApp
import com.example.cursprojectmobile.data.db.CartDao
import com.example.cursprojectmobile.data.db.DatabaseHelper
import com.example.cursprojectmobile.data.model.CartItem
import com.example.cursprojectmobile.data.model.FoodItem

class CartRepository(
    private val cartDao: CartDao = CartDao(DatabaseHelper(FoodCouriersApp.instance))
) {

    fun getCartItems(): List<CartItem> = cartDao.getCartItems()

    fun addToCart(foodId: Long, quantity: Int = 1): Boolean = cartDao.addToCart(foodId, quantity)

    fun addToCart(food: FoodItem, quantity: Int = 1): Boolean = cartDao.addToCart(food.id, quantity)

    fun updateQuantity(cartId: Long, newQuantity: Int): Boolean = cartDao.updateQuantity(cartId, newQuantity)

    fun removeFromCart(cartId: Long): Boolean = cartDao.removeFromCart(cartId)

    fun removeItem(cartId: Long): Boolean = cartDao.removeFromCart(cartId)

    fun clearCart(): Int = cartDao.clearCart()

    fun getCartCount(): Int = cartDao.getCartTotalCount()

    fun getSubtotal(): Double {
        return cartDao.getCartItems().sumOf { it.totalPrice }
    }
}
