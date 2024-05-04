package com.example.hackmatch.data.model.response

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class EventData(
    val description: String,
    val endDate: String,
    val eventType: String,
    val isOnline: Boolean,
    val link: String,
    val location: String,
    val participationType: String,
    val startDate: String,
    val teamSize: Int,
    val title: String
): Parcelable