package com.example.hackmatch.ui.onBoarding

import androidx.lifecycle.ViewModel
import com.example.hackmatch.data.local.PreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    val preferenceManager: PreferenceManager
): ViewModel() {
    fun isUserLoggedIn(): Boolean {
        return preferenceManager.fetchAuthToken() != null
    }
}