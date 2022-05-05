package com.team1.teamone.member.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.databinding.ActivityFindidBinding
import com.team1.teamone.network.FindIdRequest
import com.team1.teamone.network.MemberResponse
import com.team1.teamone.network.RetrofitService
import kotlinx.android.synthetic.main.activity_findid.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FindIdActivity : AppCompatActivity() {
    // 통신을 위해서 레트로핏 객체를 가져온다
    val api = RetrofitService.create()
    private lateinit var findId : ActivityFindidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findid)
        findId = DataBindingUtil.setContentView(this, R.layout.activity_findid)

        findId.btnFindId.setOnClickListener {
            val email = edt_email.text.toString()
            val schoolId = edt_schoolId.text.toString()

            val data = FindIdRequest(email, schoolId)

            Log.d("data", data.toString())
            api.findId(data).enqueue(object : Callback<MemberResponse> {
                override fun onResponse(call: Call<MemberResponse>, response: Response<MemberResponse>) {
                    if (response.body() == null) {
                        tv_findId.text = "웹메일 또는 학번이 잘못 되었습니다."
                        return
                    }
                    val userId = response.body()?.userId.toString()
                    tv_findId.text = email + "로 가입된 아이디는 " + userId + "입니다."
                }

                override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                    // 실패
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }

    }
}