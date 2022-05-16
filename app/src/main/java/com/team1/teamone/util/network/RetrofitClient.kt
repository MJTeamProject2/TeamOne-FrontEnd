package com.team1.teamone.util.network

import android.util.Log
import android.webkit.CookieManager
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClient {
    companion object {
        const val BASE_URL = "http://10.0.2.2:8080" // 주소

        // 로그인 인터셉터 : 생성할때 세션값을 넣어주면 헤더에 cookie : JSESSIONID=ASDASDAS 이런식으로 넣어주록 해줍니다.
        // 인터셉터의 역할은 모든 요청 메시지가 처리되기전에 호출되서 헤더에 특정 값을 넣어주는 역할을 함
        class LoginInterceptor constructor(private val tokenFromCookieManager: String) : Interceptor {
            override fun intercept(chain: Interceptor.Chain): Response = with(chain) {
                Log.d("interceptor Session", tokenFromCookieManager)
                val newRequest = request().newBuilder()
                    .addHeader("cookie", tokenFromCookieManager)
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
        fun <T> create(apiService : Class<T>) : T {
            val gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(apiService)
        }

        // create 오버로딩 : 세션값을 넣으면 해당 세션값을 인터셉트(리퀘스트 헤더에 자동으로 넣어주는) 해주는 인터셉터를 달아줌
        fun <T> create(api : Class<T>, token : String) : T {
            val gson = GsonBuilder().setLenient().create();
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(provideOkHttpClient(LoginInterceptor(token)))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(api)
        }

        // 쿠키매니저에서 쿠키정보 가져오는 편의 메소드
        fun getAuth() : String {
            return CookieManager.getInstance().getCookie(BASE_URL)
        }
    }
}