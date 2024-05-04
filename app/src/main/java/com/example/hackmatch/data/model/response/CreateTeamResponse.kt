package com.example.hackmatch.data.model.response

import androidx.annotation.Keep

@Keep
data class CreateTeamResponse(
    val success : Boolean,
    val teamId : String,
    val message:String
)