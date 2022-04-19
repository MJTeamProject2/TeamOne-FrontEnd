package com.team1.teamone.retrofit2

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
            .baseUrl("http://10.0.2.2:8080/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        api = retrofit.create(RetrofitService::class.java)
    }
}