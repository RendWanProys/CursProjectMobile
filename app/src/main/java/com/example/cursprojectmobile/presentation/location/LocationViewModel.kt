package com.example.cursprojectmobile.presentation.location

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LocationUiState(
    val selectedAddress: String = "4517 Washington Ave. Manchester, Kentucky 39495",
    val presetAddresses: List<String> = listOf(
        "4517 Washington Ave. Manchester, Kentucky 39495",
        "8502 Preston Rd. Inglewood, Maine 98380",
        "2118 Thornridge Cir. Syracuse, Connecticut 35624",
        "1901 Thornridge Cir. Shiloh, Hawaii 81063"
    )
)

class LocationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        val user = UserSessionManager.getCurrentUser()
        if (user != null && user.address.isNotEmpty()) {
            _uiState.update { it.copy(selectedAddress = user.address) }
        }
    }

    fun selectAddress(address: String) {
        _uiState.update { it.copy(selectedAddress = address) }
    }

    fun confirmLocation(onDone: () -> Unit) {
        UserSessionManager.updateAddress(_uiState.value.selectedAddress)
        onDone()
    }
}
