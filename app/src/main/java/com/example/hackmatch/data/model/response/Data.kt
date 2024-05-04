package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class Data(
    val token: String,
    val user: User
)