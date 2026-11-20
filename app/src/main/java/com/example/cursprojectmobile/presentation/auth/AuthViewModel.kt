package com.example.cursprojectmobile.presentation.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cursprojectmobile.domain.model.User
import com.example.cursprojectmobile.domain.repository.AuthFirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    // Login form
    val loginEmail: String = "",
    val loginPassword: String = "",
    val isLoginPasswordVisible: Boolean = false,
    val isLoginLoading: Boolean = false,
    val loginError: String? = null,

    // Sign Up form
    val signUpEmail: String = "",
    val signUpPassword: String = "",
    val signUpConfirmPassword: String = "",
    val isSignUpPasswordVisible: Boolean = false,
    val fullName: String = "",
    val userName: String = "",
    val phone: String = "",
    val isSignUpLoading: Boolean = false,
    val signUpError: String? = null,

    // Current user
    val currentUser: User? = null
)

class AuthViewModel(
    private val repository: AuthFirebaseRepository = AuthFirebaseRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        val savedUser = repository.getSavedUser()
        if (savedUser != null) {
            _uiState.update { it.copy(currentUser = savedUser) }
        }
    }

    fun onLoginEmailChange(email: String) {
        _uiState.update { it.copy(loginEmail = email, loginError = null) }
    }

    fun onLoginPasswordChange(password: String) {
        _uiState.update { it.copy(loginPassword = password, loginError = null) }
    }

    fun toggleLoginPasswordVisibility() {
        _uiState.update { it.copy(isLoginPasswordVisible = !it.isLoginPasswordVisible) }
    }

    fun login(onSuccess: () -> Unit) {
        val state = _uiState.value
        val email = state.loginEmail.trim()
        val password = state.loginPassword.trim()

        if (email.isEmpty()) {
            _uiState.update { it.copy(loginError = "Please enter email address") }
            return
        }
        if (password.isEmpty()) {
            _uiState.update { it.copy(loginError = "Please enter password") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoginLoading = true, loginError = null) }
            val result = repository.login(email, password)
            if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.update { it.copy(isLoginLoading = false, currentUser = user, loginError = null) }
                onSuccess()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Login failed"
                _uiState.update { it.copy(isLoginLoading = false, loginError = errorMsg) }
            }
        }
    }

    fun onSignUpEmailChange(email: String) {
        _uiState.update { it.copy(signUpEmail = email, signUpError = null) }
    }

    fun onSignUpPasswordChange(password: String) {
        _uiState.update { it.copy(signUpPassword = password, signUpError = null) }
    }

    fun onSignUpConfirmPasswordChange(confirm: String) {
        _uiState.update { it.copy(signUpConfirmPassword = confirm, signUpError = null) }
    }

    fun onFullNameChange(name: String) {
        _uiState.update { it.copy(fullName = name, signUpError = null) }
    }

    fun onUserNameChange(userName: String) {
        _uiState.update { it.copy(userName = userName, signUpError = null) }
    }

    fun onPhoneChange(phone: String) {
        _uiState.update { it.copy(phone = phone, signUpError = null) }
    }

    fun toggleSignUpPasswordVisibility() {
        _uiState.update { it.copy(isSignUpPasswordVisible = !it.isSignUpPasswordVisible) }
    }

    fun register(onSuccess: () -> Unit) {
        val state = _uiState.value
        val email = state.signUpEmail.trim()
        val password = state.signUpPassword.trim()
        val confirm = state.signUpConfirmPassword.trim()

        if (email.isEmpty()) {
            _uiState.update { it.copy(signUpError = "Please enter email address") }
            return
        }
        if (password.length < 6) {
            _uiState.update { it.copy(signUpError = "Password must be at least 6 characters") }
            return
        }
        if (confirm.isNotEmpty() && password != confirm) {
            _uiState.update { it.copy(signUpError = "Passwords do not match") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSignUpLoading = true, signUpError = null) }
            val result = repository.register(
                email = email,
                password = password,
                userName = state.userName,
                fullName = state.fullName,
                phone = state.phone
            )
            if (result.isSuccess) {
                val user = result.getOrNull()
                _uiState.update { it.copy(isSignUpLoading = false, currentUser = user, signUpError = null) }
                onSuccess()
            } else {
                val errorMsg = result.exceptionOrNull()?.message ?: "Registration failed"
                _uiState.update { it.copy(isSignUpLoading = false, signUpError = errorMsg) }
            }
        }
    }

    fun logout() {
        repository.logout()
        _uiState.update { it.copy(currentUser = null) }
    }

    fun updateAddress(address: String) {
        val current = _uiState.value.currentUser ?: return
        val updated = current.copy(address = address)
        viewModelScope.launch {
            repository.updateProfile(updated)
            _uiState.update { it.copy(currentUser = updated) }
        }
    }
}
