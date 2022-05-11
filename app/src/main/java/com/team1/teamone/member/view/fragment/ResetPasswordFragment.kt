package com.team1.teamone.member.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentResetPasswordBinding
import com.team1.teamone.network.BoolResponse
import com.team1.teamone.network.FindIdPasswordRequest
import com.team1.teamone.network.RetrofitService
import kotlinx.android.synthetic.main.fragment_find_id.*
import kotlinx.android.synthetic.main.fragment_reset_password.*
import kotlinx.android.synthetic.main.fragment_reset_password.edt_email
import kotlinx.android.synthetic.main.fragment_reset_password.edt_schoolId
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ResetPasswordFragment : Fragment() {
    val api = RetrofitService.create()
    private lateinit var binding : FragmentResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_password, container, false)

        // 임시 비밀번호 보내기
        binding.btnResetPassword.setOnClickListener {
            val schoolId = edt_schoolId.text.toString()
            val email = edt_email.text.toString()

            val request = FindIdPasswordRequest(email, schoolId)
            api.resetPassword(request).enqueue(object : Callback<BoolResponse> {
                override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                    if (response.body() == null) {
                        tv_resetPasswordResult.text = "웹메일 또는 학번이 잘못 되었습니다."
                        return
                    }
                    else if (response.code() == 200) {
                        // 메일을 서버에서 보내고 다시 응답을 주는데 까지 1~1.5초 정도 소요되는데 이걸 어케 처리하면 좋을가요...
                        tv_resetPasswordResult.text = email + "\n로 임시 비밀번호를 성공적으로 전송했습니다."
                    }
                }

                override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                    // 실패
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }

        return binding.root
    }

}