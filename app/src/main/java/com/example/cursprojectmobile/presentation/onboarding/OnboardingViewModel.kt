package com.example.cursprojectmobile.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursprojectmobile.data.UserSessionManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class OnboardingUiState(
    val appName: String = "Food Couriers",
    val splashSubtitle: String = "Delicious food delivered to your door"
)

sealed class OnboardingNavigationEvent {
    object NavigateToHome : OnboardingNavigationEvent()
    object NavigateToWelcome : OnboardingNavigationEvent()
}

class OnboardingViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private val _navigationEvent = MutableSharedFlow<OnboardingNavigationEvent>()
    val navigationEvent = _navigationEvent.asSharedFlow()

    init {
        startSplashTimer()
    }

    private fun startSplashTimer() {
        viewModelScope.launch {
            delay(1500L)
            if (UserSessionManager.isLoggedIn()) {
                _navigationEvent.emit(OnboardingNavigationEvent.NavigateToHome)
            } else {
                _navigationEvent.emit(OnboardingNavigationEvent.NavigateToWelcome)
            }
        }
    }
}
