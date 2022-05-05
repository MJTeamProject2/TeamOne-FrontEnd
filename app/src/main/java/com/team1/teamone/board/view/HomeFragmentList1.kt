package com.team1.teamone.board.view

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.FragmentHomeBinding
import com.team1.teamone.databinding.FragmentHomeList1Binding
import com.team1.teamone.member.model.Profiles
import com.team1.teamone.member.presenter.ProfileAdapter
import com.team1.teamone.network.GetFindIdModel
import com.team1.teamone.network.MemberDto
import com.team1.teamone.network.RetrofitService
import kotlinx.android.synthetic.main.activity_findid.*
import kotlinx.android.synthetic.main.fragment_home_list1.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragmentList1 : Fragment() {
    private lateinit var binding : FragmentHomeList1Binding
    val api = RetrofitService.create()
    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }*/
    lateinit var profileList: ArrayList<Profiles>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_list1, container, false)
        api.getFreeBoards().enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                val title = response.body()?.title.toString()
                profileList = arrayListOf(
                    Profiles(R.drawable.ic_baseline_bookmark_24,title,28,"안드로이드 앱 개발자"),
                    Profiles(R.drawable.ic_baseline_account_circle_24,"배성흥",25,"돼지")
                )
            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
        binding.rvProfile.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.rvProfile.setHasFixedSize(true)
        binding.rvProfile.adapter = ProfileAdapter(profileList)
        return binding.root



    }
}


/*
    val profileList = arrayListOf(
        Profiles(R.drawable.ic_baseline_bookmark_24,"홍드로이",28,"안드로이드 앱 개발자"),
        Profiles(R.drawable.ic_baseline_account_circle_24,"배성흥",25,"돼지")
    )

    binding.rvProfile.layoutManager = LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
    binding.rvProfile.setHasFixedSize(true)
    binding.rvProfile.adapter = ProfileAdapter(profileList)
    return binding.root
}*/