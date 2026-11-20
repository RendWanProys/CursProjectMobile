package com.example.cursprojectmobile.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursprojectmobile.data.UserSessionManager
import com.example.cursprojectmobile.data.model.Category
import com.example.cursprojectmobile.data.model.FoodItem
import com.example.cursprojectmobile.data.repository.CartRepository
import com.example.cursprojectmobile.data.repository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val searchQuery: String = "",
    val selectedCategory: String = "Burger",
    val categories: List<Category> = emptyList(),
    val filteredFoods: List<FoodItem> = emptyList(),
    val popularFoods: List<FoodItem> = emptyList(),
    val deliveryAddress: String = "4517 Washington Ave. Manchester, Kentucky 39495",
    val cartCount: Int = 0
)

class HomeViewModel(
    private val foodRepository: FoodRepository = FoodRepository(),
    private val cartRepository: CartRepository = CartRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        val cats = foodRepository.getCategories()
        val popular = foodRepository.getPopularItems()
        val currentCategory = _uiState.value.selectedCategory
        val foods = foodRepository.getItemsByCategory(currentCategory)
        val user = UserSessionManager.getCurrentUser()
        val count = cartRepository.getCartCount()

        _uiState.update {
            it.copy(
                categories = cats,
                popularFoods = popular,
                filteredFoods = foods,
                deliveryAddress = user?.address ?: it.deliveryAddress,
                cartCount = count
            )
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        val results = if (query.trim().isEmpty()) {
            foodRepository.getItemsByCategory(_uiState.value.selectedCategory)
        } else {
            foodRepository.searchItems(query.trim())
        }
        _uiState.update { it.copy(filteredFoods = results) }
    }

    fun selectCategory(categoryName: String) {
        _uiState.update { it.copy(selectedCategory = categoryName, searchQuery = "") }
        val foods = foodRepository.getItemsByCategory(categoryName)
        _uiState.update { it.copy(filteredFoods = foods) }
    }

    fun addToCart(foodItem: FoodItem, onAdded: () -> Unit = {}) {
        cartRepository.addToCart(foodItem, 1)
        val newCount = cartRepository.getCartCount()
        _uiState.update { it.copy(cartCount = newCount) }
        onAdded()
    }

    fun refreshSessionAddress() {
        val user = UserSessionManager.getCurrentUser()
        if (user != null) {
            _uiState.update { it.copy(deliveryAddress = user.address) }
        }
        _uiState.update { it.copy(cartCount = cartRepository.getCartCount()) }
    }
}
