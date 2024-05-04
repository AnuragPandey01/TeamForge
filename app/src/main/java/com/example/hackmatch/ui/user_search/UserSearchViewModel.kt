package com.example.hackmatch.ui.user_search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.response.AllTagResponseItem
import com.example.hackmatch.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSearchViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
): ViewModel() {

    private val _tags = MutableLiveData<List<AllTagResponseItem>>()
    val tags : LiveData<List<AllTagResponseItem>> = _tags
    private val _selectedTag = MutableLiveData<List<String>>()
    val selectedTag : LiveData<List<String>> = _selectedTag
    private val _userSearchState = MutableLiveData<UserSearchState>()
    val userSearchState : LiveData<UserSearchState> = _userSearchState

    init {
        viewModelScope.launch {
            try {
                _tags.value = apiService.getAllTags()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        searchUser()
    }

    fun searchUser() {
        _userSearchState.value = UserSearchState.Loading
        viewModelScope.launch {
            try {
                val response = apiService.getUserTags(selectedTag.value ?: emptyList())
                if (response.success) {
                    _userSearchState.value = UserSearchState.Data(response.data)
                } else {
                    _userSearchState.value = UserSearchState.Error(response.message)
                }
            } catch (e: Exception) {
                _userSearchState.value = UserSearchState.Error(e.message ?: "An error occurred")
            }
        }
    }

    fun selectTag(tag: String) {
        val currentSelectedTag = _selectedTag.value?.toMutableList() ?: mutableListOf()
        if (currentSelectedTag.contains(tag)) {
            currentSelectedTag.remove(tag)
        } else {
            currentSelectedTag.add(tag)
        }
        _selectedTag.value = currentSelectedTag
        searchUser()
    }
}

sealed class UserSearchState{
    data object Loading : UserSearchState()
    data object Idle : UserSearchState()
    data class Error(val message: String) : UserSearchState()
    data class Data(val data: List<User>) : UserSearchState()
}