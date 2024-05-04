package com.example.hackmatch.ui.contest

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.CreateTeamRequest
import com.example.hackmatch.data.model.response.EventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailContestViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager,
    val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableLiveData<DetailContestState>(DetailContestState.Idle)
    val state: LiveData<DetailContestState> = _state
    private val args = savedStateHandle.get<EventData>("eventData")!!

    fun createTeam() {
        _state.value = DetailContestState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.createTeam(
                    preferenceManager.fetchAuthToken()!!,
                    CreateTeamRequest(
                        name = args.title+" team"
                    )
                )
                if (response.success) {
                    _state.value = DetailContestState.Success(response.teamId)
                } else {
                    _state.value = DetailContestState.Error(response.message)
                }
            } catch (e: Exception) {
                Log.d(TAG, "createTeam: ${e.stackTraceToString()}")
                _state.value = DetailContestState.Error(e.message ?: "An error occurred")
            }
        }
    }

    companion object {
        private const val TAG = "DetailContestViewModel"
    }
}

sealed class DetailContestState {
    data object Idle : DetailContestState()
    data object Loading : DetailContestState()
    data class Success(val teamID : String): DetailContestState()
    data class Error(val msg:String) : DetailContestState()
}