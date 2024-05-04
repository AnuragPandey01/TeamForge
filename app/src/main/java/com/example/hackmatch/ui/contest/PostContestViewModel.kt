package com.example.hackmatch.ui.contest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.CreateEventRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostContestViewModel @Inject constructor(
    val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _state = MutableLiveData<PostContestState>(PostContestState.Idle)
    val state : LiveData<PostContestState>
        get() = _state

    fun postEvent(
        postEventRequest: CreateEventRequest
    ){
        _state.value = PostContestState.Loading
        viewModelScope.launch {
            try{
                apiService.postEvent(
                    preferenceManager.fetchAuthToken()!!,
                    postEventRequest
                )
                _state.value = PostContestState.Success
            }catch (e:Exception){
                _state.value  = PostContestState.Error(e.message?:"An error occurred")
            }
        }
    }

}

sealed class PostContestState{
    data object Loading: PostContestState()
    data object Idle: PostContestState()
    data class Error(val msg: String): PostContestState()
    data object Success: PostContestState()
}

