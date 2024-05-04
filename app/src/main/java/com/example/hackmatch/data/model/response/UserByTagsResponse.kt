package com.example.hackmatch.data.model.response

import androidx.annotation.Keep
import com.example.hackmatch.data.model.response.User

@Keep
data class UserByTagsResponse(
    val success: Boolean,
    val message: String,
    val data: List<User>
)