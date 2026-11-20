package com.example.cursprojectmobile.presentation.cart

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.UserSessionManager
import com.example.cursprojectmobile.data.model.CartItem
import com.example.cursprojectmobile.data.model.Order
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CartUiState(
    val cartItems: List<CartItem> = emptyList(),
    val subtotal: Double = 0.0,
    val deliveryFee: Double = 2.0,
    val discount: Double = 0.0,
    val total: Double = 0.0,
    val deliveryAddress: String = "4517 Washington Ave. Manchester, Kentucky 39495"
)

class CartViewModel(
    private val cartRepository: CartRepository = CartRepository(),
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(CartUiState())
    val uiState: StateFlow<CartUiState> = _uiState.asStateFlow()

    init {
        loadCart()
    }

    fun loadCart() {
        val items = cartRepository.getCartItems()
        val sub = items.sumOf { it.totalPrice }
        val fee = if (items.isNotEmpty()) 2.0 else 0.0
        val disc = if (sub > 25.0) 3.0 else 0.0
        val tot = (sub + fee - disc).coerceAtLeast(0.0)
        val user = UserSessionManager.getCurrentUser()

        _uiState.update {
            it.copy(
                cartItems = items,
                subtotal = sub,
                deliveryFee = fee,
                discount = disc,
                total = tot,
                deliveryAddress = user?.address ?: it.deliveryAddress
            )
        }
    }

    fun incrementItem(cartId: Long) {
        val item = _uiState.value.cartItems.find { it.id == cartId } ?: return
        cartRepository.updateQuantity(cartId, item.quantity + 1)
        loadCart()
    }

    fun decrementItem(cartId: Long) {
        val item = _uiState.value.cartItems.find { it.id == cartId } ?: return
        if (item.quantity > 1) {
            cartRepository.updateQuantity(cartId, item.quantity - 1)
        } else {
            cartRepository.removeItem(cartId)
        }
        loadCart()
    }

    fun removeItem(cartId: Long) {
        cartRepository.removeItem(cartId)
        loadCart()
    }

    fun placeOrder(onOrderPlaced: (Long) -> Unit) {
        val state = _uiState.value
        if (state.cartItems.isEmpty()) return

        val order = Order(
            date = System.currentTimeMillis(),
            subtotal = state.subtotal,
            deliveryFee = state.deliveryFee,
            discount = state.discount,
            totalAmount = state.total,
            address = state.deliveryAddress,
            status = "Processing",
            rating = 5,
            feedback = ""
        )

        val orderId = orderRepository.createOrder(order)
        cartRepository.clearCart()
        loadCart()
        onOrderPlaced(orderId)
    }
}
