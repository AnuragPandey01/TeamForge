package com.example.hackmatch.data.model.request

import androidx.annotation.Keep
import com.example.hackmatch.data.model.response.User

@Keep
data class UpdateProfileRequest(
    val experience: List<User.ExperienceData>,
    val tagIds: List<String>,
    val title: String
)