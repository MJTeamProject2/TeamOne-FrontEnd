package com.team1.teamone.member.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.databinding.ActivityLoginBinding
import com.team1.teamone.member.model.MemberApi
import com.team1.teamone.util.network.MemberResponseWithSession
import com.team1.teamone.util.network.LoginRequest
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import com.team1.teamone.util.view.PreferenceUtil.Companion.prefs
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val api = RetrofitClient.create(MemberApi::class.java)
    private lateinit var loginBinding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loginBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)

        // 로그인 버튼
        loginBinding.btnLogin.setOnClickListener {
            val userId = idEditArea_login.text.toString()
            val password = passwordEditArea_login.text.toString()
            val loginRequest = LoginRequest(userId,password)
            login(loginRequest)
        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
        }

        // 아이디 찾기 버튼
        btn_findId.setOnClickListener {
            val findIdIntent = Intent(this, FindActivity::class.java)
            startActivity(findIdIntent)
        }
        
        // 비밀번호 재설정 버튼
        btn_findPw.setOnClickListener {
            val resetPasswordIntent = Intent(this, FindActivity::class.java)
            startActivity(resetPasswordIntent)
        }
    }

    // 쿠키매니저에 서버로 부터 받아온 세션 저장
    private fun setSession(sessionId : String) {
        val cm: CookieManager = CookieManager.getInstance()
        cm.removeAllCookie()
        cm.setCookie(RetrofitClient.BASE_URL, sessionId)
        Log.d("sessionId From CookieManager", cm.getCookie(RetrofitClient.BASE_URL))
        Log.d("sessionId From Server", sessionId)
    }

    private fun login(loginRequest: LoginRequest) {
        api.postLogin(loginRequest).enqueue(object : Callback<MemberResponseWithSession> {
            override fun onResponse(call: Call<MemberResponseWithSession>, response: Response<MemberResponseWithSession>) {
                val sessionId = response.body()?.sessionId.toString()
                val userName = response.body()?.member?.userName.toString()
                val userid = response.body()?.member?.memberId

                // userid 저장
                prefs.setString("userid", userid.toString())
                setSession(sessionId)

                if (sessionId != "null") {
                    Toast.makeText(applicationContext, userName + "님 환영합니다 :)", Toast.LENGTH_SHORT).show()
                    val loginSuccessIntent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(loginSuccessIntent)
                    finish()
                } else {
                    Toast.makeText(applicationContext, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<MemberResponseWithSession>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")

                Toast.makeText(applicationContext, "서버와 연결이 되어있지 않습니다. 고객센터에 문의해주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}