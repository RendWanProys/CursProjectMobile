package com.example.cursprojectmobile.data.repository

import com.example.cursprojectmobile.FoodCouriersApp
import com.example.cursprojectmobile.data.db.DatabaseHelper
import com.example.cursprojectmobile.data.db.OrderDao
import com.example.cursprojectmobile.data.model.Order

class OrderRepository(
    private val orderDao: OrderDao = OrderDao(DatabaseHelper(FoodCouriersApp.instance))
) {

    fun createOrder(order: Order): Long = orderDao.createOrder(order)

    fun getAllOrders(): List<Order> = orderDao.getAllOrders()

    fun getOrderById(id: Long): Order? = orderDao.getOrderById(id)

    fun submitRating(orderId: Long, rating: Int, feedback: String): Boolean =
        orderDao.updateOrderRating(orderId, rating, feedback)

    fun updateOrderFeedback(orderId: Long, rating: Int, feedback: String): Boolean =
        submitRating(orderId, rating, feedback)
}
