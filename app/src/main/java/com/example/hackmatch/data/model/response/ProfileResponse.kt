package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class ProfileResponse(
    val success: Boolean,
    val message: String,
    val data: User?
)