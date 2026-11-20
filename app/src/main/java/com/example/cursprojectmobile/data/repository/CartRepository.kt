package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.data.db.CartDao
import com.example.cursprojectmobile.data.model.CartItem

class CartRepository(private val cartDao: CartDao) {

    fun getCartItems(): List<CartItem> = cartDao.getCartItems()

    fun addToCart(foodId: Long, quantity: Int = 1): Boolean = cartDao.addToCart(foodId, quantity)

    fun updateQuantity(cartId: Long, newQuantity: Int): Boolean = cartDao.updateQuantity(cartId, newQuantity)

    fun removeFromCart(cartId: Long): Boolean = cartDao.removeFromCart(cartId)

    fun clearCart(): Int = cartDao.clearCart()

    fun getCartCount(): Int = cartDao.getCartTotalCount()

    fun getSubtotal(): Double {
        return cartDao.getCartItems().sumOf { it.totalPrice }
    }
}
