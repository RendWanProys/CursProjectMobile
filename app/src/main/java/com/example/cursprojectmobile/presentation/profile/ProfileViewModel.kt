package com.example.cursprojectmobile.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursprojectmobile.data.UserSessionManager
import com.example.cursprojectmobile.data.model.Order
import com.example.cursprojectmobile.data.repository.OrderRepository
import com.example.cursprojectmobile.domain.model.User
import com.example.cursprojectmobile.domain.repository.AuthFirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val orders: List<Order> = emptyList(),
    val isEditMode: Boolean = false,
    val editFullName: String = "",
    val editPhone: String = "",
    val editAddress: String = ""
)

class ProfileViewModel(
    private val authRepository: AuthFirebaseRepository = AuthFirebaseRepository(),
    private val orderRepository: OrderRepository = OrderRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        val user = authRepository.getSavedUser() ?: User(
            fullName = "Anamika Sandula",
            email = "anamika@gmail.com",
            phone = "+1 234 567 890",
            address = "4517 Washington Ave. Manchester, Kentucky 39495"
        )
        val orders = orderRepository.getAllOrders()

        _uiState.update {
            it.copy(
                user = user,
                orders = orders,
                editFullName = user.fullName,
                editPhone = user.phone,
                editAddress = user.address
            )
        }
    }

    fun toggleEditMode() {
        _uiState.update { it.copy(isEditMode = !it.isEditMode) }
    }

    fun onEditFullName(name: String) {
        _uiState.update { it.copy(editFullName = name) }
    }

    fun onEditPhone(phone: String) {
        _uiState.update { it.copy(editPhone = phone) }
    }

    fun onEditAddress(address: String) {
        _uiState.update { it.copy(editAddress = address) }
    }

    fun saveProfile() {
        val current = _uiState.value.user ?: return
        val updated = current.copy(
            fullName = _uiState.value.editFullName,
            phone = _uiState.value.editPhone,
            address = _uiState.value.editAddress
        )

        viewModelScope.launch {
            authRepository.updateProfile(updated)
            _uiState.update { it.copy(user = updated, isEditMode = false) }
        }
    }

    fun logout(onLoggedOut: () -> Unit) {
        authRepository.logout()
        onLoggedOut()
    }
}
