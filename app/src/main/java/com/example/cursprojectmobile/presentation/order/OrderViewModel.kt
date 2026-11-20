package com.example.cursprojectmobile.presentation.order

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.model.Order
import com.example.cursprojectmobile.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class OrderUiState(
    val currentOrder: Order? = null,
    val rating: Int = 5,
    val feedback: String = "",
    val ordersHistory: List<Order> = emptyList()
)

class OrderViewModel(
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(OrderUiState())
    val uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun loadOrder(orderId: Long) {
        val order = orderRepository.getOrderById(orderId)
        val history = orderRepository.getAllOrders()
        _uiState.update { it.copy(currentOrder = order, ordersHistory = history, rating = 5, feedback = "") }
    }

    fun setRating(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    fun setFeedback(feedback: String) {
        _uiState.update { it.copy(feedback = feedback) }
    }

    fun submitFeedback(onDone: () -> Unit) {
        val order = _uiState.value.currentOrder
        if (order != null) {
            orderRepository.updateOrderFeedback(order.id, _uiState.value.rating, _uiState.value.feedback)
        }
        onDone()
    }
}
