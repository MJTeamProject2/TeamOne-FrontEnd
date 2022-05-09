package com.team1.teamone.network

import android.util.Log
import android.webkit.CookieManager
import com.google.gson.GsonBuilder
import com.team1.teamone.board.model.BoardResponse
import okhttp3.Interceptor
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.Response
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

    @POST("/boards/new/free")
    @Headers("accept: application/json",
        "content-type: application/json")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

//    @POST("/boards/new/free/no-login")
//    @Headers("accept: application/json",
//        "content-type: application/json")
//    fun postFreeBoard(
//        @Body freeBoardRequestForm : FreeBoardRequest
//    ): Call<BoardResponse>

    companion object { // static 처럼 공유객체로 사용가능함. 모든 인스턴스가 공유하는 객체로서 동작함.
        private const val BASE_URL = "http://10.0.2.2:8080" // 주소

        // 로그인 인터셉터 : 생성할때 세션값을 넣어주면 헤더에 cookie : JSESSIONID=ASDASDAS 이런식으로 넣어주록 해줍니다.
        // 인터셉터의 역할은 모든 요청 메시지가 처리되기전에 호출되서 헤더에 특정 값을 넣어주는 역할을 함
        class LoginInterceptor constructor(private val tokenFromCookieManager: String) : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
                Log.d("interceptor Session", tokenFromCookieManager)
                val newRequest = request().newBuilder()
                    .addHeader("cookie", "JESSIONID="+tokenFromCookieManager)
                    .build()
                proceed(newRequest)
            }
        }

        // 클라이언트에 인터셉터 탑재하기
        private fun provideOkHttpClient(interceptor: LoginInterceptor): OkHttpClient
                = OkHttpClient.Builder().run {
            addInterceptor(interceptor)
            build()
        }
        
        // 이건 그냥 디폴트 생성자
        fun create(): RetrofitService {
            val gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }
        
        // create 오버로딩 : 세션값을 넣으면 해당 세션값을 인터셉트(리퀘스트 헤더에 자동으로 넣어주는) 해주는 인터셉터를 달아줌
        fun create(token : String) : RetrofitService{
            val gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient(LoginInterceptor(token)))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(RetrofitService::class.java)
        }

        // 쿠키매니저에서 쿠키정보 가져오는 편의 메소드
        fun getAuth() : String {
            return CookieManager.getInstance().getCookie(BASE_URL)
        }
    }
}