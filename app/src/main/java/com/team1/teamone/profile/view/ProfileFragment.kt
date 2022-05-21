package com.team1.teamone.profile.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.bookmark.view.BookMarkActivity
import com.team1.teamone.caution.view.CautionActivity
import com.team1.teamone.databinding.FragmentProfileBinding
import com.team1.teamone.profile.model.ProfileApi
import com.team1.teamone.util.network.MemberResponse
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil.Companion.prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    val api = RetrofitClient.create(ProfileApi::class.java, RetrofitClient.getAuth())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userid = prefs.getString("userid",  "")
        Log.d("userid", userid)

        setMemberData(userid)

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.btnProfileEdit.setOnClickListener {
            val profileEditIntent = Intent(context, ProfileDetailActivity::class.java)
            startActivity(profileEditIntent)
        }

        binding.btnBookMark.setOnClickListener {
            val bookMarkIntent = Intent(context, BookMarkActivity::class.java)
            startActivity(bookMarkIntent)
        }

        binding.btnWrittenBoard.setOnClickListener {
            val writtenBoardIntent = Intent(context, WrittenBoardActivity::class.java)
            startActivity(writtenBoardIntent)
        }

        binding.btnCautionList.setOnClickListener {
            val cautionListIntent = Intent(context, CautionActivity::class.java)
            startActivity(cautionListIntent)
        }

        return binding.root
    }

    private fun setMemberData(userid: String) {
        api.getMember(userid).enqueue(object : Callback<MemberResponse> {
            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                val userName = response.body()?.userName
                // 프로필 메인 회원 정보 띄우기
                binding.profileName.text = userName
                binding.tvProfileEmail.text = response.body()?.email
                binding.tvProfileNickname.text = response.body()?.nickname
                binding.tvProfileDepartmnet.text = response.body()?.department
                binding.tvProfileSchoolId.text = response.body()?.schoolId
                binding.tvProfilePoint.text = response.body()?.points.toString()
            }

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                // 실패
                Log.d("log", t.message.toString())
                Log.d("log", "fail")
            }

        })
    }
}