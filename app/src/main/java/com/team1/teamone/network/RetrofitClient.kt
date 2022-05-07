package com.team1.teamone.network

import com.google.gson.GsonBuilder
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager

object RetrofitClient {
    var api: RetrofitService

    val gson = GsonBuilder()
        .setLenient()
        .create()

    init {
        val okHttpClient = OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://f7e6-121-125-152-82.jp.ngrok.io")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}