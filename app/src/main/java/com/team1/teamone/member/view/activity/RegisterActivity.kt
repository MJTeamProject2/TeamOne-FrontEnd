package com.team1.teamone.member.view.activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.util.view.MainActivity
import com.team1.teamone.databinding.ActivityRegisterBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.member.model.MemberApi
import com.team1.teamone.util.network.*
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(MemberApi::class.java)
    private lateinit var register: ActivityRegisterBinding
    private var TAG: String = "Register"
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register = DataBindingUtil.setContentView(this, R.layout.activity_register)

        // 사용자 아이디 중복 체크
        register.btnCheckID.setOnClickListener {
            val id = rt_id.text.toString()
            Log.d("btnCheckID", id)
            if (id.isNotEmpty()) {
                val idDuplicateCheck = idDuplicateCheck(id, isCheckUserId)
                isCheckUserId = idDuplicateCheck
                Log.d("bbbbbbb", isCheckUserId.toString())
            } else {
                dialog("error blank")
            }
        }

        // 닉네임 중복 체크
        register.btnCheckNickname.setOnClickListener {
            val name = rt_nickname.text.toString()
            Log.d("btnCheckNickname", name)
            if (name.isNotEmpty()) {
                val nickNameDuplicateCheck = nickNameDuplicateCheck(name, isCheckNickName)
                isCheckNickName = nickNameDuplicateCheck
            } else {
                dialog("error blank")
            }
        }

        // 회원가입 인증메일 보내기
        register.btnSendKeyNum.setOnClickListener {
            val email = rt_email.text.toString()
            Log.d("btnSendKeyNum", email)
            register.btnSendKeyNum.isEnabled = false
            if (email.isNotEmpty()) {
                sendAuthMail(email, emailCheck, tokenCheck)
            } else {
                register.btnSendKeyNum.isEnabled = true
                dialog("error blank")
            }
        }
        
        // 회원가입 인증 토큰 검사
        register.btnCheckKeyNum.setOnClickListener {
            val email = rt_email.text.toString()
            val keyCheck = rt_keyNum.text.toString()

            Log.d("btnCheckKeyNum", keyCheck)
            register.btnCheckKeyNum.isEnabled = false
            if (keyCheck.isNotEmpty()) {
                val authTokenCheck =
                    authTokenCheck(email, keyCheck, emailCheck, tokenCheck, isCheckToken)

                isCheckToken = authTokenCheck
            } else {
                register.btnCheckKeyNum.isEnabled = true
                dialog("error blank")
            }
        }

        // 회원가입 최종 진행
        register.btnRegister2.setOnClickListener {
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
            val keyCheck = rt_keyNum.text.toString()

            val dataModel = RegisterRequest(
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
            if(nickname.length in 2..10) {
                isNickCount = true
            }
            if(id.length in 4..10) {
                isIdCount = true
            }
            if(pw.length in 6..10) {
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

                // 유저가 입력한 id, pw를 쉐어드에 저장한다.
                val sharedPreference = getSharedPreferences("file name", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("id", id)
                editor.putString("pw", pw)
                editor.apply()

                // 회원가입 진행
                register(dataModel)
                
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
            }
        }
    }

    private fun register(dataModel: RegisterRequest) {
        api.postRegister(dataModel).enqueue(object : Callback<MemberResponse> {
            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                Log.d("log", response.toString())
                Log.d("log", response.body().toString())
                Log.d("log response memberId create..", response.body()?.memberId.toString())

                if (response.body() != null) {
                    // 로그인 화면으로 이동
                    val intent = Intent(applicationContext, LoginActivity::class.java)
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

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }

        })
    }

    private fun authTokenCheck(
        email: String,
        keyCheck: String,
        emailCheck: String,
        tokenCheck: String,
        isCheckToken2: Boolean
    ): Boolean {
        var isCheckToken1 = isCheckToken2
        api.getAuthTokenCheck(email, keyCheck).enqueue(object : Callback<AuthMailResponse> {
            override fun onResponse(
                call: Call<AuthMailResponse>,
                response: Response<AuthMailResponse>
            ) {
                Log.d("btnCheckKeyNum", "success")
                val authToken = response.body()?.authToken
                val userEmail = response.body()?.userEmail
                Log.d("btnCheckKeyNum authToken", authToken.toString())

                val emailChecking : Boolean = email == userEmail
                val tokenChecking = (keyCheck == authToken)

                Log.d("emailCheck", emailChecking.toString())
                Log.d("userEmail", userEmail.toString())
                Log.d("tokenCheck", tokenChecking.toString())
                Log.d("authToken", authToken.toString())

                isCheckToken1 = emailChecking && tokenChecking
                Log.d("btnCheckKeyNum", isCheckToken1.toString())

                if (isCheckToken1) {
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
                        isCheckToken = true
                    }
                } else {
                    register.btnCheckKeyNum.isEnabled = true
                    dialog("error check token")
                }

            }

            override fun onFailure(call: Call<AuthMailResponse>, t: Throwable) {
                // 실패
                Log.d("btnCheckKeyNum", t.message.toString())
                Log.d("btnCheckKeyNum", "fail")
                register.btnCheckKeyNum.isEnabled = true
            }

        })
        return isCheckToken1
    }

    private fun sendAuthMail(
        email: String,
        emailCheck: String,
        tokenCheck: String
    ) {
        var emailCheck1 = emailCheck
        var tokenCheck1 = tokenCheck
        api.postSendAuthMail(email).enqueue(object : Callback<AuthMailResponse> {
            override fun onResponse(
                call: Call<AuthMailResponse>,
                response: Response<AuthMailResponse>
            ) {
                Log.d("btnSendKeyNum", "success")
                val authToken = response.body()?.authToken
                val userEmail = response.body()?.userEmail

                emailCheck1 = userEmail.toString()
                tokenCheck1 = authToken.toString()

                Log.d("btnSendKeyNum authToken", authToken.toString())
                Log.d("btnSendKeyNum userEmail", userEmail.toString())
                register.btnSendKeyNum.isEnabled = true
            }

            override fun onFailure(call: Call<AuthMailResponse>, t: Throwable) {
                // 실패
                Log.d("btnSendKeyNum", t.message.toString())
                Log.d("btnSendKeyNum", "fail")
                register.btnSendKeyNum.isEnabled = true
            }
        })
    }

    private fun nickNameDuplicateCheck(name: String, isCheckNickName1: Boolean): Boolean {
        var isCheckNickName2 = isCheckNickName1
        api.getNickNameCheck(name).enqueue(object : Callback<BoolResponse> {
            override fun onResponse(
                call: Call<BoolResponse>,
                response: Response<BoolResponse>
            ) {
                Log.d("btnCheckNickname", "success")
                val body = response.body()?.result
                isCheckNickName2 = !body!!
                Log.d("btnCheckNickname", isCheckNickName1.toString())

                if (isCheckNickName2) {

                    // editText 비활성화
                    register.rtNickname.setTextIsSelectable(false)
                    register.rtNickname.isFocusable = false

                    register.btnCheckNickname.isEnabled = false
                    isCheckNickName = true

                } else {
                    dialog("error check nickname")
                }
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("btnCheckNickname", t.message.toString())
                Log.d("btnCheckNickname", "fail")
            }
        })
        return isCheckNickName1
    }

    private fun idDuplicateCheck(id: String, isCheckUserId123: Boolean): Boolean {
        var isCheckUserId1 = isCheckUserId123
        var isCheckUserId22 = false
        api.getUserIdCheck(id).enqueue(object : Callback<BoolResponse> {
            override fun onResponse(
                call: Call<BoolResponse>,
                response: Response<BoolResponse>
            ) {
                Log.d("btnCheckID", "success")
                val body = response.body()?.result
                if (body != null) {
                    isCheckUserId1 = body
                }
                Log.d("btnCheckID", isCheckUserId1.toString())

                if (isCheckUserId1) {
                    // editText 비활성화
                    register.rtId.setTextIsSelectable(false)
                    register.rtId.isFocusable = false

                    register.btnCheckID.isEnabled = false
                    isCheckUserId = true

                } else {
                    dialog("error check userid")
                }
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("btnCheckID", t.message.toString())
                Log.d("btnCheckID", "fail")
            }

        })

        Log.d("aaaaaa", isCheckUserId22.toString())
        return isCheckUserId22
    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        when (type) {
            "blank" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("입력란을 모두 작성해 주세요.")
            }
            // 입력한 비밀번호가 다를 경우
            "not same" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("비밀번호가 다릅니다.")
            }
            "id lack" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("아이디는 4~10자리로 지정해 주세요.")
            }
            "nickname lack" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("닉네임은 2~10자리로 지정해 주세요.")
            }
            "password lack" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("비밀번호는 6~10자리로 지정해 주세요.")
            }
            "duplicate userid" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("ID 중복을 확인해 주세요.")
            }
            "duplicate nickname" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("닉네임 중복을 확인해 주세요.")
            }
            "duplicate email" -> {
                dialog.setTitle("이메일 중복")
                dialog.setMessage("이미 가입된 이메일 입니다.")
            }
            "error token" -> {
                dialog.setTitle("회원 가입 실패")
                dialog.setMessage("인증 번호를 확인해 주세요.")
            }
            "error check userid" -> {
                dialog.setTitle("중복된 아이디")
                dialog.setMessage("이미 가입된 아이디 입니다.")
            }
            "error check nickname" -> {
                dialog.setTitle("중복된 닉네임")
                dialog.setMessage("이미 가입된 닉네임 입니다.")
            }
            "error check token" -> {
                dialog.setTitle("인증 번호 불일치")
                dialog.setMessage("인증 번호를 확인해 주세요.")
            }
            "error blank" -> {
                dialog.setTitle("빈칸 입니다")
                dialog.setMessage("입력 칸에 내용을 입력해 주세요.")
            }
            "error server" -> {
                dialog.setTitle("알 수 없는 오류")
                dialog.setMessage("관리자에게 문의 하세요.")
            }
        }

        val dialogListener = DialogInterface.OnClickListener { _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE ->
                    Log.d(TAG, "dialog message")
            }
        }

        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }

}