package com.team1.teamone.member.model

import com.team1.teamone.util.network.*
import retrofit2.Call
import retrofit2.http.*

interface MemberApi {
    @POST("/users/login")
    fun postLogin(
        @Body loginRequestForm: LoginRequest
    ): Call<MemberResponseWithSession>

    @POST("/users/new")
    fun postRegister(
        @Body registerRequestForm: RegisterRequest
    ): Call<MemberResponse>

    @GET("/users/nickname-check/{nickname}")
    fun getNickName(
        @Path("nickname") nickName : String
    ): Call<BoolResponse>

    @GET("/users/id-check/{id}")
    fun getUserId(
        @Path("id") userId : String
    ): Call<BoolResponse>

    @POST("/users/auth/{userEmail}")
    fun postSendMail(
        @Path("userEmail") userEmail : String
    ): Call<AuthMailResponse>

    @GET("/users/auth/{userEmail}/{authToken}")
    fun getCheckToken(
        @Path("userEmail") userEmail : String,
        @Path("authToken") authToken : String
    ): Call<AuthMailResponse>

    @POST("/users/id")
    fun findId(
        @Body findIdPasswordRequestForm: FindIdPasswordRequest
    ): Call<MemberResponse>

    @POST("/users/password")
    fun resetPassword(
        @Body resetPasswordForm : FindIdPasswordRequest
    ): Call<BoolResponse>
}