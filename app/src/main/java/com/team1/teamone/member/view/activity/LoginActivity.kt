package com.team1.teamone.member.view.activity

import android.app.AlertDialog
import android.content.DialogInterface
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
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val api = RetrofitClient.create(MemberApi::class.java)
    private lateinit var login: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = DataBindingUtil.setContentView(this, R.layout.activity_login)


        // 로그인 버튼
        login.btnLogin.setOnClickListener {
            val id = idEditArea_login.text.toString()
            val pw = passwordEditArea_login.text.toString()

            val data = LoginRequest(id,pw)

            api.postLogin(data).enqueue(object : Callback<MemberResponseWithSession> {
                override fun onResponse(call: Call<MemberResponseWithSession>, response: Response<MemberResponseWithSession>) {
                    val sessionKey = response.body()?.sessionId.toString()
                    val userName = response.body()?.member?.userName.toString()
                    Toast.makeText(applicationContext, userName + "님 환영합니다 :)", Toast.LENGTH_SHORT).show()

                    val cm : CookieManager = CookieManager.getInstance()
                    cm.removeAllCookie()
                    cm.setCookie(RetrofitClient.BASE_URL, sessionKey)

                    Log.d("sessionId From CookieManager", cm.getCookie(RetrofitClient.BASE_URL))
                    Log.d("sessionId From Server", sessionKey)
                    if (sessionKey != "null") {
                        val loginSuccessIntent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(loginSuccessIntent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "아이디 또는 비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<MemberResponseWithSession>, t: Throwable) {
                    // 실패
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")

                    Toast.makeText(applicationContext, "서버와 연결이 되어있지 않습니다. 고객센터에 문의해주세요.", Toast.LENGTH_SHORT).show()
                }
            })

        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        btn_findId.setOnClickListener {
            val intent = Intent(this, FindActivity::class.java)
            startActivity(intent)
        }

        btn_findPw.setOnClickListener {
            val intent = Intent(this, FindActivity::class.java)
            startActivity(intent)
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        if(type == "success"){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type == "fail"){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        val dialogListener = DialogInterface.OnClickListener { dialog, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> Log.d("응애", "응애")
            }
        }

        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }
}