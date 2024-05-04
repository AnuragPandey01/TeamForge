package com.example.hackmatch.ui.contest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.response.EventData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class ContestViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
): ViewModel() {

    private val _state = MutableLiveData<ContestFragmentState>(ContestFragmentState.Idle)
    val state : LiveData<ContestFragmentState>
        get() = _state

    init{
        _state.value = ContestFragmentState.Loading
        viewModelScope.launch {
            try{
                val res = apiService.getAllEvents()
                if(res.success){
                    _state.value = ContestFragmentState.Data(res.data)
                }else{
                    _state.value = ContestFragmentState.Error(res.message)
                }
            }catch (e:Exception){
                _state.value = ContestFragmentState.Error(e.message?:"An error occurred")
            }
        }
    }
}

sealed class ContestFragmentState{
    data object Loading: ContestFragmentState()
    data object Idle: ContestFragmentState()
    data class Data(val events: List<EventData>):ContestFragmentState()
    data class Error(val msg:String): ContestFragmentState()
}