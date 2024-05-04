package com.example.hackmatch.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _state = MutableLiveData<ProfileState>(ProfileState.Idle)
    val state: LiveData<ProfileState>
    get() = _state

    init{
        _state.value = ProfileState.Loading
        viewModelScope.launch {
            try{
                val res = apiService.getMe(preferenceManager.fetchAuthToken()!!)
                if(res.success){
                    _state.value = ProfileState.Data(res.data!!)
                }else{
                    _state.value = ProfileState.Error(res.message)
                }
            }catch(e: Exception){
                _state.value = ProfileState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun logout(){
        preferenceManager.clearAll()
    }
}

sealed class ProfileState{
    data object Idle: ProfileState()
    data object Loading: ProfileState()
    data class Data(val user : User): ProfileState()
    data class Error(val message: String): ProfileState()
}