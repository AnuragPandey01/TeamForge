package com.example.hackmatch.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hackmatch.data.api.ApiService
import com.example.hackmatch.data.local.PreferenceManager
import com.example.hackmatch.data.model.request.UpdateProfileRequest
import com.example.hackmatch.data.model.response.AllTagResponseItem
import com.example.hackmatch.data.model.response.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SetProfileViewModel @Inject constructor(
    private val apiService: ApiService,
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _state = MutableLiveData<SetProfileState>(SetProfileState.Idle)
    val state: LiveData<SetProfileState>
        get() = _state

    private val _experienceData = MutableLiveData<List<User.ExperienceData>>(emptyList())
    val experienceData : LiveData<List<User.ExperienceData>>
        get() = _experienceData

    private val _userTags = MutableLiveData<List<AllTagResponseItem>>(emptyList())
    val tagData: LiveData<List<AllTagResponseItem>>
        get() = _userTags

    private val _selectedTags = MutableLiveData<Set<AllTagResponseItem>>(emptySet())
    val selectedTags: LiveData<Set<AllTagResponseItem>>
        get() = _selectedTags

    init{
        viewModelScope.launch {
            try{
                val res = apiService.getAllTags()
                _userTags.value = res
            }catch (e:Exception){
                _state.value = SetProfileState.Error("unable to load tags")
            }
        }
    }

    fun updateProfile(
        title:String,
        selectedTags: Set<AllTagResponseItem>,
        experienceList: List<User.ExperienceData>
    ){
        viewModelScope.launch {
            _state.value = SetProfileState.Loading
            try{
                val res = apiService.updateProfile(
                    preferenceManager.fetchAuthToken()!!,
                    UpdateProfileRequest(
                        experience = experienceList,
                        tagIds = selectedTags.map{it.id},
                        title = title
                    )
                )
                if(res.success){
                    _state.value = SetProfileState.Success
                }else{
                    _state.value = SetProfileState.Error(res.message)
                }
            }catch (e:Exception){
                _state.value = SetProfileState.Error(e.message ?: "some error occurred")
            }
        }
    }

    fun removeSelectedTag(item:AllTagResponseItem){
        _selectedTags.value = selectedTags.value!!.toMutableSet().apply { remove(item) }
    }

    fun addSelectedTag(item : String){
        if(_userTags.value!!.firstOrNull{ it.name == item } == null) return
        _selectedTags.value = selectedTags.value!!.toMutableSet().apply { add(_userTags.value!!.first{ it.name == item }) }
    }

    fun addExperience(item:User.ExperienceData){
        _experienceData.value = experienceData.value!!.toMutableList().apply { add(item) }
    }
}

sealed class SetProfileState{
    data object Idle: SetProfileState()
    data object Loading: SetProfileState()
    data object Success: SetProfileState()
    data class Error(val msg: String): SetProfileState()
}