package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class User(
    val email: String,
    val id: String,
    val instituteName: String?,
    val name: String,
    val role: String?,
    val tags: List<TagData>,
    val experiences: List<ExperienceData>,
    val iconUrl: String,
    val teams : List<TeamData>
){
    @Keep
    data class TagData(
        val id: String,
        val name: String
    )

    @Keep
    data class ExperienceData(
        val id: String = "",
        val title: String,
        val description: String,
        val link: String,
        val startDate : String,
        val endDate : String
    )

    @Keep
    data class TeamData(
        val teamId: String,
        val teamName:String,
        val isLeader: Boolean,
        val invitationAccepted: Boolean,
        val invitationDenied: Boolean,
        val userId: String,
        val userName:String,
        val userIconUrl: String,
        val teamSize: Int,
    )
}