package com.example.cursprojectmobile.presentation.detail

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DetailUiState(
    val foodItem: FoodItem? = null,
    val quantity: Int = 1
)

class DetailViewModel(
    private val foodRepository: FoodRepository = FoodRepository(),
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState())
    val uiState: StateFlow<DetailUiState> = _uiState.asStateFlow()

    fun loadFoodItem(foodId: Long) {
        val item = foodRepository.getItemById(foodId)
        _uiState.update { it.copy(foodItem = item, quantity = 1) }
    }

    fun incrementQuantity() {
        _uiState.update { it.copy(quantity = it.quantity + 1) }
    }

    fun decrementQuantity() {
        if (_uiState.value.quantity > 1) {
            _uiState.update { it.copy(quantity = it.quantity - 1) }
        }
    }

    fun addToCart(onAdded: () -> Unit) {
        val food = _uiState.value.foodItem ?: return
        val qty = _uiState.value.quantity
        cartRepository.addToCart(food, qty)
        onAdded()
    }
}
