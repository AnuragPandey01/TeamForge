package com.example.hackmatch.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.LoginRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _state: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
    val state = _state.asStateFlow()

    fun login(username: String, password: String) {
        if (username.isEmpty()) {
            _state.update {
                LoginState.ValidationError(nameError = "username is required")
            }
            return
        }
        if (password.isEmpty()) {
            _state.update {
                LoginState.ValidationError(passwordError = "password is required")
            }
            return
        }
        _state.value = LoginState.Loading

        viewModelScope.launch {
            try {
                val res = apiService.login(LoginRequest(username, password))
                if (res.success) {
                    _state.update { LoginState.Success }
                    preferenceManager.apply {
                        saveAuthToken(res.data!!.token)
                        saveProfileIconUrl(res.data.user.iconUrl)
                        saveUserId(res.data.user.id)
                    }
                } else {
                    _state.update { LoginState.Error(res.message) }
                }
            } catch (e: Exception) {
                _state.update {
                    LoginState.Error(e.message ?: "An error occurred")
                }
            }
        }
    }
}

sealed class LoginState {
    data object Idle : LoginState()
    data object Loading : LoginState()
    data object Success : LoginState()
    data class Error(val message: String) : LoginState()
    data class ValidationError(val nameError: String? = null, val passwordError: String? = null) :
        LoginState()
}