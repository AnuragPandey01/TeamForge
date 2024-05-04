package com.example.hackmatch.ui.team

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
class TeamDetailViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
): ViewModel() {

    private val _state = MutableLiveData<TeamDetailState>(TeamDetailState.Idle)
    val state: LiveData<TeamDetailState>
        get() = _state

    fun getTeamInfo(teamId: String) {
        _state.value = TeamDetailState.Loading
        viewModelScope.launch {
            try{
                val response = apiService.getTeamInfo(preferenceManager.fetchAuthToken()!!, teamId)
                _state.value = TeamDetailState.Success(response,preferenceManager.fetchUserId()!!)
            } catch(e: Exception) {
                _state.value = TeamDetailState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class TeamDetailState {
    data object Loading: TeamDetailState()
    data object Idle: TeamDetailState()
    data class Success(val teamInfo: List<User.TeamData>, val userId: String): TeamDetailState()
    data class Error(val message: String): TeamDetailState()
}