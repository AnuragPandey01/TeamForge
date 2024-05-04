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
class TeamViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _teamState = MutableLiveData<TeamState>(TeamState.Idle)
    val teamState: LiveData<TeamState>
        get() = _teamState

    init{
        _teamState.value = TeamState.Loading
        viewModelScope.launch {
            try{
                val res = apiService.getMe(preferenceManager.fetchAuthToken()!!)
                if(res.success){
                    _teamState.value = TeamState.Data(res.data!!.teams)
                }else{
                    _teamState.value = TeamState.Error(res.message)
                }
            }catch (e: Exception){
                _teamState.value = TeamState.Error(e.message ?: "An error occurred")
            }
        }
    }
}

sealed class TeamState {
    data object Idle : TeamState()
    object Loading : TeamState()
    data class Data(val data: List<User.TeamData>) : TeamState()
    data class Error(val message: String) : TeamState()
}