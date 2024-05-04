package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class AllEventResponse(
    val data: List<EventData>,
    val message: String,
    val success: Boolean
)