package com.example.hackmatch.ui.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide.init
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _state = MutableLiveData<InvitationScreenState>(InvitationScreenState.Idle)
    val state : LiveData<InvitationScreenState>
        get() = _state

    init{
        getInvitations()
    }

    fun acceptTeamInvitation(teamId:String){
        viewModelScope.launch {
            try{
                val res = apiService.acceptTeamInvitation(preferenceManager.fetchAuthToken()!!,teamId)
                getInvitations()
            }catch (e:Exception){
                _state.value = InvitationScreenState.Error(e.message?:"some error occurred")
            }
        }
    }

    fun rejectTeamInvitation(teamId:String){
        viewModelScope.launch {
            try{
                apiService.rejectTeamInvitation(preferenceManager.fetchAuthToken()!!,teamId)
                getInvitations()
            }catch (e:Exception){
                _state.value = InvitationScreenState.Error(e.message?:"some error occurred")
            }
        }
    }

    private fun getInvitations(){
        viewModelScope.launch {
            try{
                val res = apiService.getMe(preferenceManager.fetchAuthToken()!!)
                if(res.success){
                    _state.value = InvitationScreenState.Data(res.data!!.teams.filter { !it.invitationAccepted && !it.invitationDenied})
                }else{
                    _state.value = InvitationScreenState.Error(res.message?:"some error occurred")
                }
            }catch (e:Exception){
                _state.value = InvitationScreenState.Error(e.message?:"some error occurred")
            }
        }
    }
}

sealed class InvitationScreenState{
    data object Idle: InvitationScreenState()
    data object Loading: InvitationScreenState()
    data class Data(val invites: List<User.TeamData> ): InvitationScreenState()
    data class Error(val msg:String): InvitationScreenState()
}