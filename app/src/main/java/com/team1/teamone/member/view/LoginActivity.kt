package com.team1.teamone.member.view

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
import com.team1.teamone.databinding.ActivityLoginBinding
import com.team1.teamone.network.PostMemberDto
import com.team1.teamone.network.PostLoginModel
import com.team1.teamone.network.RetrofitService
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"
    val api = RetrofitService.create()
    private val BASE_URL = "http://10.0.2.2:8080/"
    private lateinit var login: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login = DataBindingUtil.setContentView(this, R.layout.activity_login)


        // 로그인 버튼
        login.btnLogin.setOnClickListener {

            //editText로부터 입력된 값을 받아온다
            var id = idEditArea_login.text.toString()
            var pw = passwordEditArea_login.text.toString()

            val data = PostLoginModel(id,pw)


            api.postLogin(data).enqueue(object : Callback<PostMemberDto> {
                override fun onResponse(call: Call<PostMemberDto>, response: Response<PostMemberDto>) {
                    Log.d("log",response.toString())
                    Log.d("log", response.body().toString())
                    val sessionKey = response.body()?.sessionId.toString()
                    Toast.makeText(applicationContext, "connection success", Toast.LENGTH_SHORT).show()

                    val cm : CookieManager = CookieManager.getInstance()
                    cm.setCookie(BASE_URL, sessionKey)

                    Log.d("sessionId", cm.getCookie(BASE_URL))
                    Log.d("sessionId2", sessionKey)
                    if (sessionKey != "null") {
                        val loginSuccessIntent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(loginSuccessIntent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "error id or pw", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<PostMemberDto>, t: Throwable) {
                    // 실패
                    Log.d("log",t.message.toString())
                    Log.d("log","fail")

                    Toast.makeText(applicationContext, "connection fail", Toast.LENGTH_SHORT).show()
                }
            })

        }

        // 회원가입 버튼
        btn_register.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}