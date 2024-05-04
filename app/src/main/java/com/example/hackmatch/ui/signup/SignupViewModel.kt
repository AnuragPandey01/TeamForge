package com.example.hackmatch.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.SignupRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel(){

    private val _signupState = MutableLiveData<SignupState>()
    val signupState : LiveData<SignupState> = _signupState

    fun signup(email:String,password:String,confirmPassword:String,instituteName:String?,name:String){
        if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()){
            _signupState.value = SignupState.InvalidInput("Please fill in all the fields")
            return
        }
        if(password != confirmPassword){
            _signupState.value = SignupState.InvalidInput("Passwords do not match")
            return
        }
        _signupState.value = SignupState.Loading
        viewModelScope.launch {
            try{
                val response = apiService.register(
                    SignupRequest(
                        email = email,
                        password = password,
                        instituteName = instituteName,
                        name = name
                    )
                )
                if(response.success){
                    preferenceManager.apply {
                        saveAuthToken(response.data!!.token)
                        saveUserId(response.data.user.id)
                        saveProfileIconUrl(response.data.user.iconUrl)
                    }
                    _signupState.value = SignupState.Success
                }else{
                    _signupState.value = SignupState.Error(response.message)
                }
            }catch(e:Exception){
                _signupState.value = SignupState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class SignupState(){
    data object Idle: SignupState()
    data object Loading: SignupState()
    data object Success: SignupState()
    data class Error(val message: String): SignupState()
    data class InvalidInput(val message: String): SignupState()
}