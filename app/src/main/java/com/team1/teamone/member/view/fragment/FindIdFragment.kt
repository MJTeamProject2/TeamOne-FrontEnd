package com.team1.teamone.member.view.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentFindIdBinding
import com.team1.teamone.member.model.MemberApi
import com.team1.teamone.util.network.FindIdPasswordRequest
import com.team1.teamone.util.network.MemberResponse
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.fragment_find_id.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FindIdFragment : Fragment() {
//    val api = RetrofitService.create()
    val api = RetrofitClient.create(MemberApi::class.java)
    private lateinit var binding : FragmentFindIdBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_id, container, false)

        binding.btnFindId.setOnClickListener {
            val email = edt_email.text.toString()
            val schoolId = edt_schoolId.text.toString()

            val request = FindIdPasswordRequest(email, schoolId)

            Log.d("data", request.toString())
            api.findId(request).enqueue(object : Callback<MemberResponse> {
                override fun onResponse(call: Call<MemberResponse>, response: Response<MemberResponse>) {
                    if (response.body() == null) {
                        tv_result_findId.text = "웹메일 또는 학번이 잘못 되었습니다."
                        return
                    }
                    val userId = response.body()?.userId.toString()
                    tv_result_findId.text = email + "로 가입된 아이디는 " + userId + "입니다."
                }

                override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                    // 실패
                    Log.d("log", t.message.toString())
                    Log.d("log", "fail")
                }
            })
        }

        return binding.root
    }

}