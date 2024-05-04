package com.example.hackmatch.data.model.request

import androidx.annotation.Keep

@Keep
data class SignupRequest(
    val email: String,
    val instituteName: String? = null,
    val name: String,
    val password: String
)