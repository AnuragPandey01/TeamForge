package com.example.hackmatch.data.api


import com.example.hackmatch.data.model.request.AcceptInvitationRequest
import com.example.hackmatch.data.model.request.CreateEventRequest
import com.example.hackmatch.data.model.request.CreateTeamRequest
import com.example.hackmatch.data.model.request.InviteUserRequest
import com.example.hackmatch.data.model.request.LoginRequest
import com.example.hackmatch.data.model.request.SignupRequest
import com.example.hackmatch.data.model.request.UpdateProfileRequest
import com.example.hackmatch.data.model.response.AllEventResponse
import com.example.hackmatch.data.model.response.AllTagResponseItem
import com.example.hackmatch.data.model.response.AuthResponse
import com.example.hackmatch.data.model.response.CreateTeamResponse
import com.example.hackmatch.data.model.response.ProfileResponse
import com.example.hackmatch.data.model.response.User
import com.example.hackmatch.data.model.response.UserByTagsResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @POST("user/register")
    suspend fun register(
        @Body
        signupRequest: SignupRequest
    ): AuthResponse

    @POST("user/login")
    suspend fun login(
        @Body
        loginRequest: LoginRequest
    ): AuthResponse

    @POST("user/profile")
    suspend fun updateProfile(
        @Header("Authorization") token: String,
        @Body updateProfileRequest: UpdateProfileRequest
    ): AuthResponse

    @GET("tag/all")
    suspend fun getAllTags(): List<AllTagResponseItem>

    @GET("event/all")
    suspend fun getAllEvents(): AllEventResponse

    @POST("user/tags")
    suspend fun getUserTags(
        @Body tags: List<String>
    ): UserByTagsResponse

    @GET("user/profile")
    suspend fun getProfile(
        @Query("userId") userId: String,
    ): ProfileResponse

    @POST("team/invite")
    suspend fun inviteToTeam(
        @Header("Authorization") token: String,
        @Body inviteUserRequest: InviteUserRequest
    )

    @POST("team/create")
    suspend fun createTeam(
        @Header("Authorization") token: String,
        @Body createTeamRequest: CreateTeamRequest
    ) : CreateTeamResponse

    @GET("user/me")
    suspend fun getMe(
        @Header("Authorization") token: String
    ): ProfileResponse

    @POST("team/info")
    suspend fun getTeamInfo(
        @Header("Authorization") token: String,
        @Query("teamId") teamId: String
    ): List<User.TeamData>

    @POST("event/create")
    suspend fun postEvent(
        @Header("Authorization") token: String,
        @Body createEventRequest: CreateEventRequest
    )

    @POST("team/accept")
    suspend fun acceptTeamInvitation(
        @Header("Authorization") token: String,
        @Query("teamId") teamId: String
    )

    @POST("team/reject")
    suspend fun rejectTeamInvitation(
        @Header("Authorization") token: String,
        @Query("teamId") teamId: String
    )

}

