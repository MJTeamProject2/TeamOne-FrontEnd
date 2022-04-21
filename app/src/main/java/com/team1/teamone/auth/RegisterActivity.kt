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
import com.team1.teamone.retrofit2.MemberDto
import com.team1.teamone.retrofit2.PostMemberDto
import com.team1.teamone.retrofit2.PostRegisterModel
import com.team1.teamone.retrofit2.RetrofitService
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
        register = DataBindingUtil.setContentView(this, R.layout.activity_register)

        register.btnRegister2.setOnClickListener {
            Log.d(TAG, "cc 버튼 클릭")
            var isExistBlank = false
            var isPWSame = false
            var isNickCount = false
            var isIdCount = false
            var isPassCount = false
            var isEmail = false

            val name = rt_name.text.toString()
            val dept = rt_department.text.toString()
            val schoolId = rt_schoolId.text.toString()
            val phoneNum = rt_phoneNumber.text.toString()
            val nickname = rt_nickname.text.toString()
            val id = rt_id.text.toString()
            val pw = rt_password.text.toString()
            val checkPass = rt_checkPass.text.toString()
            val email = rt_email.text.toString() + "@mju.ac.kr"
            var keyCheck = rt_keyNum.text.toString()

            val dataModel = PostRegisterModel(
                name,
                dept,
                schoolId,
                phoneNum,
                nickname,
                id,
                pw,
                checkPass,
                email,
                keyCheck
            )


            //유저가 항목을 다 채우지 않았을 경우
            if (name.isEmpty() || dept.isEmpty() || schoolId.isEmpty() || phoneNum.isEmpty() || nickname.isEmpty() || id.isEmpty() || pw.isEmpty() || checkPass.isEmpty() || email.isEmpty() || keyCheck.isEmpty()) {
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


            if (!isExistBlank && isPWSame && isNickCount && isIdCount && isPassCount ) {
                Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

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

                        // 로그인 화면으로 이동
                        val intent = Intent(applicationContext, MainActivity::class.java)
                        startActivity(intent)
                    }

                    override fun onFailure(call: Call<MemberDto>, t: Throwable) {
                        // 실패
                        Log.d("log",t.message.toString())
                        Log.d("log","fail")
                    }

                })



            } else {

                // 상태에 따라 다른 다이얼로그 띄워주기
                if (isExistBlank) {   // 작성 안한 항목이 있을 경우
                    dialog("blank")
                } else if (!isPWSame) { // 입력한 비밀번호가 다를 경우
                    dialog("not same")
                } else if(!isIdCount) {
                    dialog("id lack")
                } else if(!isNickCount) {
                    dialog("nickname lack")
                } else if(!isPassCount) {
                    dialog("password lack")
                }
//                else if(!isEmail) {
//                    dialog("email error")
//                }
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