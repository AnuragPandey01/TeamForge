package com.example.hackmatch.data.model.request

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class CreateEventRequest(
    val description: String,
    val endDate: String,
    val eventType: EventType,
    val isOnline: Boolean,
    val link: String,
    val location: String?,
    val participationType: ParticipationType,
    val startDate: String,
    val teamSize: Int?,
    val title: String
){
    enum class ParticipationType{
        @SerializedName("INDIVIDUAL") INDIVIDUAL,
        @SerializedName("TEAM") TEAM
    }

    enum class EventType{
        @SerializedName("HACKATHON") HACKATHON,
        @SerializedName("SEMINAR") SEMINAR,
        @SerializedName("WORKSHOP") WORKSHOP
    }
}