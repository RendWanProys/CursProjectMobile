package com.example.cursprojectmobile.presentation.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.lifecycle.ViewModel
import com.example.cursprojectmobile.data.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

data class StreetPoint(
    val name: String,
    val address: String,
    val xPct: Float,
    val yPct: Float
)

data class LocationUiState(
    val selectedAddress: String = "109, Freedom Way, Off Lekki Phase Victoria Island, Lagos.",
    val pinXPct: Float = 0.50f,
    val pinYPct: Float = 0.44f,
    val searchQuery: String = "",
    val isDetectingGps: Boolean = false
)

class LocationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LocationUiState())
    val uiState: StateFlow<LocationUiState> = _uiState.asStateFlow()

    val knownStreets = listOf(
        StreetPoint("Balasubramaniam Salai", "Balasubramaniam Salai, KK Nagar, Chennai", 0.50f, 0.44f),
        StreetPoint("87th Street", "87th Street, Sector 7, KK Nagar, Chennai", 0.44f, 0.27f),
        StreetPoint("84th Street", "84th Street, Sector 6, KK Nagar, Chennai", 0.63f, 0.31f),
        StreetPoint("Kamarajar Salai", "Kamarajar Salai, Sector 8, KK Nagar, Chennai", 0.79f, 0.31f),
        StreetPoint("85th Street", "85th Street, Sector 9, KK Nagar, Chennai", 0.84f, 0.35f),
        StreetPoint("88th Street", "88th Street, Sector 10, KK Nagar, Chennai", 0.94f, 0.27f),
        StreetPoint("PT Rajan Salai", "PT Rajan Salai, Sector 1, KK Nagar, Chennai", 0.16f, 0.31f),
        StreetPoint("Bobbili Raja Salai", "Bobbili Raja Salai, Sector 2, KK Nagar, Chennai", 0.26f, 0.31f),
        StreetPoint("Jeevanandan Salai", "Jeevanandan Salai, Sector 3, KK Nagar, Chennai", 0.35f, 0.31f),
        StreetPoint("Sivan Park", "Sivan Park, KK Nagar, Chennai", 0.12f, 0.20f),
        StreetPoint("103rd Street", "103rd Street, Sector 12, KK Nagar, Chennai", 0.25f, 0.09f),
        StreetPoint("16th Avenue", "16th Avenue, Sector 5, KK Nagar, Chennai", 0.90f, 0.48f),
        StreetPoint("Kendriya Vidyalaya", "Kendriya Vidyalaya, Ashok Nagar, Chennai", 0.85f, 0.63f),
        StreetPoint("Ashok Pillar Road", "Ashok Pillar Road, Main Junction, KK Nagar, Chennai", 0.36f, 0.72f),
        StreetPoint("MTC Bus Station", "MTC Bus Station, Central KK Nagar, Chennai", 0.20f, 0.82f),
        StreetPoint("6th Street", "6th Street, Sector 11, KK Nagar, Chennai", 0.22f, 0.93f),
        StreetPoint("15th Avenue", "15th Avenue, Sector 4, KK Nagar, Chennai", 0.96f, 0.91f),
        StreetPoint("Freedom Way", "109, Freedom Way, Off Lekki Phase Victoria Island, Lagos.", 0.50f, 0.44f)
    )

    init {
        val user = UserSessionManager.getCurrentUser()
        if (user != null && user.address.isNotEmpty()) {
            _uiState.update { it.copy(selectedAddress = user.address) }
        }
    }

    fun onMapTapped(xPct: Float, yPct: Float) {
        var closest = knownStreets.first()
        var minDist = Float.MAX_VALUE
        for (st in knownStreets) {
            val dx = st.xPct - xPct
            val dy = st.yPct - yPct
            val dist = dx * dx + dy * dy
            if (dist < minDist) {
                minDist = dist
                closest = st
            }
        }

        _uiState.update {
            it.copy(
                pinXPct = xPct.coerceIn(0.10f, 0.90f),
                pinYPct = yPct.coerceIn(0.20f, 0.75f),
                selectedAddress = closest.address,
                searchQuery = closest.name
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        if (query.isNotBlank()) {
            val match = knownStreets.find {
                it.name.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true)
            }
            if (match != null) {
                _uiState.update {
                    it.copy(
                        selectedAddress = match.address,
                        pinXPct = match.xPct,
                        pinYPct = match.yPct
                    )
                }
            } else {
                _uiState.update { it.copy(selectedAddress = query) }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun detectDeviceLocation(context: Context) {
        _uiState.update { it.copy(isDetectingGps = true) }
        var resolved = false
        try {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            val location: Location? = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                ?: locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)

            if (location != null) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        geocoder.getFromLocation(location.latitude, location.longitude, 1) { addresses ->
                            val addr = addresses.firstOrNull()?.getAddressLine(0)
                            if (addr != null) {
                                _uiState.update {
                                    it.copy(
                                        selectedAddress = addr,
                                        searchQuery = addr,
                                        pinXPct = 0.50f,
                                        pinYPct = 0.44f,
                                        isDetectingGps = false
                                    )
                                }
                            }
                        }
                        resolved = true
                    } else {
                        @Suppress("DEPRECATION")
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val addr = addresses?.firstOrNull()?.getAddressLine(0)
                        if (addr != null) {
                            _uiState.update {
                                it.copy(
                                    selectedAddress = addr,
                                    searchQuery = addr,
                                    pinXPct = 0.50f,
                                    pinYPct = 0.44f,
                                    isDetectingGps = false
                                )
                            }
                            resolved = true
                        }
                    }
                } catch (e: Exception) {
                    // ignore
                }
            }
        } catch (e: Exception) {
            // ignore
        }

        if (!resolved) {
            // Immediate responsive fallback GPS detection
            _uiState.update {
                it.copy(
                    selectedAddress = "Balasubramaniam Salai, KK Nagar, Chennai",
                    pinXPct = 0.50f,
                    pinYPct = 0.44f,
                    searchQuery = "Balasubramaniam Salai",
                    isDetectingGps = false
                )
            }
        }
    }

    fun confirmLocation(onDone: () -> Unit) {
        UserSessionManager.updateAddress(_uiState.value.selectedAddress)
        onDone()
    }
}
