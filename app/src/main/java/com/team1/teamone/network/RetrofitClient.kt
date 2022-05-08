//package com.team1.teamone.network
//
//import com.google.gson.GsonBuilder
//import okhttp3.Interceptor
//import okhttp3.JavaNetCookieJar
//import okhttp3.OkHttpClient
//import okhttp3.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//object RetrofitClient {
//    var api: RetrofitService
//
//    val gson = GsonBuilder()
//        .setLenient()
//        .create()
//
//    init {
//        val okHttpClient = OkHttpClient.Builder()
////            .cookieJar(JavaNetCookieJar(CookieManager()))
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://f7e6-121-125-152-82.jp.ngrok.io")
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create(gson))
//            .build()
//
//        api = retrofit.create(RetrofitService::class.java)
//    }
//
//    private val appInterceptor = object : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
//            val newRequest = request().newBuilder()
//                .addHeader("key", "session")
//                .build()
//            proceed(newRequest)
//        }
//    }
//}