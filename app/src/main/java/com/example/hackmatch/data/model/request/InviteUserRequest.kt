package com.example.hackmatch.data.model.request

import androidx.annotation.Keep

@Keep
data class InviteUserRequest(
    val userId: String,
    val teamId: String
)