package com.example.hackmatch.data.local

import android.content.Context

class PreferenceManager(
    private val context: Context
) {

    private val sharedPreferences = context.getSharedPreferences("hackmatch", Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        sharedPreferences.edit().putString("token", "bearer "+token).apply()
    }

    fun fetchAuthToken(): String? {
        return sharedPreferences.getString("token", null)
    }

    fun clearAuthToken() {
        sharedPreferences.edit().remove("token").apply()
    }

    fun saveProfileIconUrl(url: String) {
        sharedPreferences.edit().putString("profile_icon_url", url).apply()
    }

    fun fetchProfileIconUrl(): String? {
        return sharedPreferences.getString("profile_icon_url", null)
    }

    fun clearProfileIconUrl() {
        sharedPreferences.edit().remove("profile_icon_url").apply()
    }

    fun saveUserId(userId: String) {
        sharedPreferences.edit().putString("user_id", userId).apply()
    }

    fun fetchUserId(): String? {
        return sharedPreferences.getString("user_id", null)
    }

    fun clearUserId() {
        sharedPreferences.edit().remove("user_id").apply()
    }

    fun clearAll() {
        sharedPreferences.edit().clear().apply()
    }
}