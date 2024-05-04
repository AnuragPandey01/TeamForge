package com.example.hackmatch.ui.user_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.InviteUserRequest
import com.example.hackmatch.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InviteViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
): ViewModel() {

    private val teamId = savedStateHandle.get<String>("teamID")!!
    private val userId = savedStateHandle.get<String>("userID")!!
    private val _state = MutableLiveData<InviteScreenState>(InviteScreenState.Idle)
    val state: LiveData<InviteScreenState>
        get() = _state

    init{
        _state.value = InviteScreenState.Loading
        viewModelScope.launch {
            try {
                val res = apiService.getProfile(userId)
                if(res.success){
                    _state.value = InviteScreenState.Data(res.data!!)
                }else{
                    _state.value = InviteScreenState.Error(res.message)
                }
            }catch (e: Exception) {
                _state.value = InviteScreenState.Error(e.message?: "An error occurred")
            }
        }
    }

    fun inviteUser(){
        _state.value = InviteScreenState.InviteSending
        viewModelScope.launch {
            try {
                apiService.inviteToTeam(
                    preferenceManager.fetchAuthToken()!!,
                    InviteUserRequest(
                        userId = userId,
                        teamId = teamId
                    )
                )
                _state.value = InviteScreenState.InviteSent("Invite sent successfully")
            }catch (e: Exception) {
                _state.value = InviteScreenState.Error(e.message?: "An error occurred")
            }
        }
    }

}

sealed class InviteScreenState {
    data object Idle : InviteScreenState()
    data object Loading : InviteScreenState()
    data class Data(val data: User) : InviteScreenState()
    data class Error(val error: String) : InviteScreenState()
    data class InviteSent(val message: String) : InviteScreenState()
    data class InviteError(val error: String) : InviteScreenState()
    data object InviteSending : InviteScreenState()
}