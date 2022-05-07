package com.team1.teamone.network

import com.google.gson.GsonBuilder
import com.team1.teamone.board.model.BoardResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface RetrofitService {

    @POST("/users/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLogin(
        @Body loginRequestForm: LoginRequest
    ): Call<MemberResponseWithSession>

    @POST("/users/new")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postRegister(
        @Body registerRequestForm: RegisterRequest
    ): Call<MemberResponse>

    @GET("/users/nickname-check/{nickname}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getNickName(
        @Path("nickname") nickName : String
    ): Call<BoolResponse>

    @GET("/users/id-check/{id}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getUserId(
        @Path("id") userId : String
    ): Call<BoolResponse>

    @POST("/users/auth/{userEmail}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postSendMail(
        @Path("userEmail") userEmail : String
    ): Call<AuthMailResponse>

    @GET("/users/auth/{userEmail}/{authToken}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getCheckToken(
        @Path("userEmail") userEmail : String,
        @Path("authToken") authToken : String
    ): Call<AuthMailResponse>

    @POST("/users/id")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun findId(
        @Body findIdPasswordRequestForm: FindIdPasswordRequest
    ): Call<MemberResponse>

    @POST("/users/password")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun resetPassword(
        @Body resetPasswordForm : FindIdPasswordRequest
    ): Call<BoolResponse>

    @GET("/boards/all")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getAllBoards(
    ): Call<BoardResponse>


    @GET("/boards/{boardId}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getBoard(
        @Path("boardId") boardId : Long
    ): Call<BoardResponse>
/*
    @POST("/boards/new/free")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>
*/
    @POST("/boards/new/free/no-login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "https://f7e6-121-125-152-82.jp.ngrok.io" // 주소

        fun create(): RetrofitService {
            val gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                //                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }
    }
}