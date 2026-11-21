package com.example.cursprojectmobile.presentation.location

import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AddressLocation(
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val label: String
)

data class LocationUiState(
    val selectedAddress: String = "4517 Washington Ave. Manchester, Kentucky 39495",
    val selectedLatitude: Double = 37.1537,
    val selectedLongitude: Double = -83.7619,
    val presetLocations: List<AddressLocation> = listOf(
        AddressLocation(
            address = "4517 Washington Ave. Manchester, Kentucky 39495",
            latitude = 37.1537,
            longitude = -83.7619,
            label = "Home"
        ),
        AddressLocation(
            address = "8502 Preston Rd. Inglewood, Maine 98380",
            latitude = 43.6615,
            longitude = -70.2553,
            label = "Office"
        ),
        AddressLocation(
            address = "2118 Thornridge Cir. Syracuse, Connecticut 35624",
            latitude = 41.7658,
            longitude = -72.6734,
            label = "Parents"
        ),
        AddressLocation(
            address = "1901 Thornridge Cir. Shiloh, Hawaii 81063",
            latitude = 21.3069,
            longitude = -157.8583,
            label = "Vacation Villa"
        )
    )
)

class LocationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    init {
        val user = UserSessionManager.getCurrentUser()
        if (user != null && user.address.isNotEmpty()) {
            val matching = _uiState.value.presetLocations.find { it.address.equals(user.address, ignoreCase = true) }
            if (matching != null) {
                _uiState.update {
                    it.copy(
                        selectedAddress = matching.address,
                        selectedLatitude = matching.latitude,
                        selectedLongitude = matching.longitude
                    )
                }
            } else {
                _uiState.update { it.copy(selectedAddress = user.address) }
            }
        }
    }

    fun selectAddress(address: String) {
        val matching = _uiState.value.presetLocations.find { it.address.equals(address, ignoreCase = true) }
        val lat = matching?.latitude ?: 37.1537
        val lon = matching?.longitude ?: -83.7619
        _uiState.update {
            it.copy(
                selectedAddress = address,
                selectedLatitude = lat,
                selectedLongitude = lon
            )
        }
    }

    fun confirmLocation(onDone: () -> Unit) {
        UserSessionManager.updateAddress(_uiState.value.selectedAddress)
        onDone()
    }
}
