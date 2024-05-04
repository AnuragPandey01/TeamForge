package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class AuthResponse(
    val data: Data?,
    val message: String,
    val success: Boolean
)