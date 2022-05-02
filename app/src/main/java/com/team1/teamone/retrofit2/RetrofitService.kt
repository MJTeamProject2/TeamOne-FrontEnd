package com.team1.teamone.retrofit2

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface RetrofitService {

    @POST("/users/login")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postLogin(
        @Body jsonparams: PostLoginModel
    ): Call<PostMemberDto>

    @POST("/users/new")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postRegister(
        @Body jsonparams: PostRegisterModel
    ): Call<MemberDto>

    @GET("/users/nickname-check/{nickname}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getNickName(
        @Path("nickname") nickName : String
    ): Call<GetBoolean>

    @GET("/users/id-check/{id}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getUserId(
        @Path("id") userId : String
    ): Call<GetBoolean>

    @POST("/users/auth/{userEmail}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postSendMail(
        @Path("userEmail") userEmail : String
    ): Call<PostAuthEmail>

    @GET("/users/auth/{userEmail}/{authToken}")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun getCheckToken(
        @Path("userEmail") userEmail : String,
        @Path("authToken") authToken : String
    ): Call<GetAuthToken>

    @POST("/users/id")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun findId(
        @Body jsonparams: GetFindIdModel
    ): Call<MemberDto>


    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://10.0.2.2:8080/" // 주소

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