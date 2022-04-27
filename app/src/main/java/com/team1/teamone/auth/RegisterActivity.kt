package com.team1.teamone.auth

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.team1.teamone.MainActivity
import com.team1.teamone.R
import com.team1.teamone.databinding.ActivityRegisterBinding
import com.team1.teamone.retrofit2.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    val api = RetrofitService.create()
    private val BASE_URL = "http://10.0.2.2:8080/"
    private lateinit var register: ActivityRegisterBinding
    var TAG: String = "Register"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        var isExistBlank = false
        var isPWSame = false
        var isNickCount = false
        var isIdCount = false
        var isPassCount = false
        var isCheckUserId = false
        var isCheckNickName = false
        var isCheckToken = false
        var emailCheck = ""
        var tokenCheck = ""

        register = DataBindingUtil.setContentView(this, R.layout.activity_register)

        register.btnCheckID.setOnClickListener {
            val id = rt_id.text.toString()
            Log.d("btnCheckID", id)
            if (id.isNotEmpty()) {
                api.getUserId(id).enqueue(object : Callback<GetBoolean> {
                    override fun onResponse(
                        call: Call<GetBoolean>,
                        response: Response<GetBoolean>
                    ) {
                        Log.d("btnCheckID", "success")
                        val body = response.body()?.result
                        isCheckUserId = !body!!
                        Log.d("btnCheckID", isCheckUserId.toString())

                        if (isCheckUserId) {
                            // editText 비활성화
                            register.rtId.setTextIsSelectable(false)
                            register.rtId.isFocusable = false

                            register.btnCheckID.isEnabled = false
                        } else {
                            dialog("error check userid")
                        }

                    }

                    override fun onFailure(call: Call<GetBoolean>, t: Throwable) {
                        // 실패
                        Log.d("btnCheckID", t.message.toString())
                        Log.d("btnCheckID", "fail")
                    }

                })
            } else {
                dialog("error blank")
            }
        }

        register.btnCheckNickname.setOnClickListener {
            val name = rt_nickname.text.toString()
            Log.d("btnCheckNickname", name)
            if (name.isNotEmpty()) {
                api.getNickName(name).enqueue(object : Callback<GetBoolean> {
                    override fun onResponse(
                        call: Call<GetBoolean>,
                        response: Response<GetBoolean>
                    ) {
                        Log.d("btnCheckNickname", "success")
                        val body = response.body()?.result
                        isCheckNickName = !body!!
                        Log.d("btnCheckNickname", isCheckNickName.toString())

                        if (isCheckNickName) {

                            // editText 비활성화
                            register.rtNickname.setTextIsSelectable(false)
                            register.rtNickname.isFocusable = false

                            register.btnCheckNickname.isEnabled = false
                        } else {
                            dialog("error check nickname")
                        }


                    }

                    override fun onFailure(call: Call<GetBoolean>, t: Throwable) {
                        // 실패
                        Log.d("btnCheckNickname", t.message.toString())
                        Log.d("btnCheckNickname", "fail")
                    }
                })
            } else {
                dialog("error blank")
            }
        }

        register.btnSendKeyNum.setOnClickListener {
            val email = rt_email.text.toString()
            Log.d("btnSendKeyNum", email)
            register.btnSendKeyNum.isEnabled = false
            if (email.isNotEmpty()) {
                api.postSendMail(email).enqueue(object : Callback<PostAuthEmail> {
                    override fun onResponse(
                        call: Call<PostAuthEmail>,
                        response: Response<PostAuthEmail>
                    ) {
                        Log.d("btnSendKeyNum", "success")
                        val authToken = response.body()?.authToken
                        val userEmail = response.body()?.userEmail

                        emailCheck = userEmail.toString()!!
                        tokenCheck = authToken.toString()!!

                        Log.d("btnSendKeyNum authToken", authToken.toString())
                        Log.d("btnSendKeyNum userEmail", userEmail.toString())
                        register.btnSendKeyNum.isEnabled = true
                    }

                    override fun onFailure(call: Call<PostAuthEmail>, t: Throwable) {
                        // 실패
                        Log.d("btnSendKeyNum", t.message.toString())
                        Log.d("btnSendKeyNum", "fail")
                        register.btnSendKeyNum.isEnabled = true
                    }
                })
            } else {
                register.btnSendKeyNum.isEnabled = true
                dialog("error blank")
            }
        }

        register.btnCheckKeyNum.setOnClickListener {
            val email = rt_email.text.toString()
            var keyCheck = rt_keyNum.text.toString()

            Log.d("btnCheckKeyNum", keyCheck)
            register.btnCheckKeyNum.isEnabled = false
            if (keyCheck.isNotEmpty()) {
                api.getCheckToken(email,keyCheck).enqueue(object : Callback<GetAuthToken> {
                    override fun onResponse(
                        call: Call<GetAuthToken>,
                        response: Response<GetAuthToken>
                    ) {
                        Log.d("btnCheckKeyNum", "success")
                        val authToken = response.body()?.authToken
                        val userEmail = response.body()?.userEmail
                        Log.d("btnCheckKeyNum authToken", authToken.toString())

                        val emailChecking = emailCheck == userEmail
                        val tokenChecking = tokenCheck == authToken

                        Log.d("emailCheck", emailCheck.toString())
                        Log.d("userEmail", userEmail.toString())
                        Log.d("tokenCheck", tokenCheck.toString())
                        Log.d("authToken", authToken.toString())

                        isCheckToken = emailChecking && tokenChecking
                        Log.d("btnCheckKeyNum", isCheckToken.toString())

                        if (isCheckToken) {
                            if (response.body()?.authToken == null) {
                                Log.d("btnCheckKeyNum duplicate email ", "duplicate email")
                                register.btnCheckKeyNum.isEnabled = true
                                dialog("duplicate email")
                            } else {
                                // editText 비활성화
                                register.rtEmail.setTextIsSelectable(false)
                                register.rtEmail.isFocusable = false

                                register.rtKeyNum.setTextIsSelectable(false)
                                register.rtKeyNum.isFocusable = false

                                register.btnSendKeyNum.isEnabled = false
                                register.btnCheckKeyNum.isEnabled = false
                            }
                        } else {
                            register.btnCheckKeyNum.isEnabled = true
                            dialog("error check token")
                        }

                    }

                    override fun onFailure(call: Call<GetAuthToken>, t: Throwable) {
                        // 실패
                        Log.d("btnCheckKeyNum", t.message.toString())
                        Log.d("btnCheckKeyNum", "fail")
                        register.btnCheckKeyNum.isEnabled = true
                    }

                })
            } else {
                register.btnCheckKeyNum.isEnabled = true
                dialog("error blank")
            }
        }

        register.btnRegister2.setOnClickListener {

//            var isEmail = false
            val name = rt_name.text.toString()
            val dept = rt_department.text.toString()
            val schoolId = rt_schoolId.text.toString()
            val phoneNum = rt_phoneNumber.text.toString()
            val nickname = rt_nickname.text.toString()
            val id = rt_id.text.toString()
            val pw = rt_password.text.toString()
            val checkPass = rt_checkPass.text.toString()
            val email = rt_email.text.toString()
            //val email = rt_email.text.toString() + "@mju.ac.kr"
            var keyCheck = rt_keyNum.text.toString()

            val dataModel = PostRegisterModel(
                name, dept, schoolId, phoneNum, nickname, id, pw, checkPass, email, keyCheck
            )


            Log.d("testsetname", name.isNotBlank().toString())
            Log.d("testsetdept", dept.isNotBlank().toString())
            Log.d("testsetschoolId", schoolId.isNotBlank().toString())
            Log.d("testsetphoneNum", phoneNum.isNotBlank().toString())
            Log.d("testsetnickname", nickname.isNotBlank().toString())

            //유저가 항목을 다 채우지 않았을 경우
            if (name.isNotBlank() && dept.isNotBlank() &&
                schoolId.isNotBlank() && phoneNum.isNotBlank() &&
                nickname.isNotBlank() && id.isNotBlank() &&
                pw.isNotBlank() && checkPass.isNotBlank() &&
                email.isNotBlank() && keyCheck.isNotBlank()) {
                isExistBlank = true
            }
            if (pw == checkPass) { //비밀번호와 비밀번호 확인이 동일하지 않은 경우
                isPWSame = true
            }
            if(nickname.length >= 2 && nickname.length <= 10) {
                isNickCount = true
            }
            if(id.length >= 4 && id.length <= 10) {
                isIdCount = true
            }
            if(pw.length >= 6 && pw.length <= 10) {
                isPassCount = true
            }
//            if(email.contains("@")) {
//                isEmail = true
//            }
            Log.d("tftf_isExistBlank", isExistBlank.toString())
            Log.d("tftf_isPWSame", isPWSame.toString())
            Log.d("tftf_isNickCount", isNickCount.toString())
            Log.d("tftf_isIdCount", isIdCount.toString())
            Log.d("tftf_isPassCount", isPassCount.toString())
            Log.d("tftf_isCheckUserId", isCheckUserId.toString())
            Log.d("tftf_isCheckNickName", isCheckNickName.toString())
            Log.d("tftf_isCheckToken", isCheckToken.toString())



            if (isExistBlank && isPWSame &&
                isNickCount && isIdCount &&
                isPassCount && isCheckNickName &&
                isCheckUserId && isCheckToken) {
//                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                api.postRegister(dataModel).enqueue(object : Callback<MemberDto> {
                    override fun onResponse(call: Call<MemberDto>, response: Response<MemberDto>) {
                        Log.d("log",response.toString())
                        Log.d("log", response.body().toString())

                        if (response.body() != null) {
                            // 로그인 화면으로 이동
                            val intent = Intent(applicationContext, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            register.rtEmail.setTextIsSelectable(true)
                            register.rtEmail.isFocusable = true
                            register.rtKeyNum.setTextIsSelectable(true)
                            register.rtKeyNum.isFocusable = true
                            register.btnCheckKeyNum.isEnabled = true
                            register.btnSendKeyNum.isEnabled = true
                            dialog("duplicate email")

                        }


                    }

                    override fun onFailure(call: Call<MemberDto>, t: Throwable) {
                        // 실패
                        Log.d("log",t.message.toString())
                        Log.d("log","fail")
                    }

                })



            } else {

                // 상태에 따라 다른 다이얼로그 띄워주기
                if (!isExistBlank) {   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                } else if (!isPWSame) { // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                } else if(!isIdCount) {
                    dialog("id lack")
                } else if(!isNickCount) {
                    dialog("nickname lack")
                } else if(!isPassCount) {
                    dialog("password lack")
                } else if(!isCheckUserId) {
                    dialog("duplicate userid")
                } else if(!isCheckNickName) {
                    dialog("duplicate nickname")
                } else if(!isCheckToken) {
                    dialog("error token")
                }
//                else if(!isEmail) {
//                    dialog("email error")
//                }
                isExistBlank = false
                isPWSame = false
                isNickCount = false
                isIdCount = false
                isPassCount = false
//                isCheckUserId = false
//                isCheckNickName = false
//                isCheckToken = false
            }


        }
    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }
        // 입력한 비밀번호가 다를 경우
        else if(type.equals("not same")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호가 다릅니다")
        }

        else if(type.equals("id lack")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("아이디는 4~10자리로 지정해주세요")
        }

        else if(type.equals("nickname lack")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("닉네임은 2~10자리로 지정해주세요")
        }

        else if(type.equals("password lack")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("비밀번호는 6~10자리로 지정해주세요")
        }

        else if(type.equals("duplicate userid")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("ID 중복을 확인해주세요")
        }

        else if(type.equals("duplicate nickname")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("닉네임 중복을 확인해 주세요")
        }

        else if(type.equals("duplicate email")){
            dialog.setTitle("이메일 중복")
            dialog.setMessage("이미 가입된 이메일 입니다.")
        }

        else if(type.equals("error token")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("인증 번호를 확인해 주세요")
        }

        else if(type.equals("error check userid")){
            dialog.setTitle("중복된 아이디")
            dialog.setMessage("이미 가입된 아이디 입니다.")
        }

        else if(type.equals("error check nickname")){
            dialog.setTitle("중복된 닉네임")
            dialog.setMessage("이미 가입된 닉네임 입니다.")
        }

        else if(type.equals("error check token")){
            dialog.setTitle("인증 번호 불일치")
            dialog.setMessage("인증 번호를 확인해 주세요")
        }

        else if(type.equals("error blank")){
            dialog.setTitle("빈칸 입니다.")
            dialog.setMessage("입력 칸에 내용을 입력해 주세요")
        }

        else if(type.equals("error server")){
            dialog.setTitle("알 수 없는 오류")
            dialog.setMessage("관리자에게 문의 하세요")
        }



        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }

}