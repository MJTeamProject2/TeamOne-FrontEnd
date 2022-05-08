package com.team1.teamone.board.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.FragmentHomeList1Binding
import com.team1.teamone.board.presenter.BoardAdapter
import com.team1.teamone.network.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragmentList1 : Fragment() {
    private lateinit var binding : FragmentHomeList1Binding
    val api = RetrofitService.create()
    private var boardList: ArrayList<BoardResponse> = ArrayList<BoardResponse>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home_list1, container, false)
        binding.btnTest.setOnClickListener{
            val intent = Intent(getActivity(), WriteFreeBoardActivity::class.java)
            startActivity(intent)
        }
        api.getBoard(3).enqueue(object : Callback<BoardResponse> {

            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
//                print("tlqkf")
//                Log.d("response", response.message())
//                val boards = response.body()?.boards
//                Log.d("size", boardList.size.toString())
//                //for (i in boards.orEmpty()) {
//                    Log.d("dmd",i.title.toString())
////                    profileList.add(Profiles(R.drawable.ic_baseline_bookmark_24, i.title.toString()+"1", 28, "안드로이드 앱 개발자"))
//                    boardList.add(boards[0])
//                //}
                val board = response.body()
                if (board != null) {
                    boardList.add(board)
                }

                binding.rvProfile.layoutManager = LinearLayoutManager(this@HomeFragmentList1.context, LinearLayoutManager.VERTICAL, false)
                binding.rvProfile.setHasFixedSize(true)
                binding.rvProfile.adapter = BoardAdapter(boardList)

            }


            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {

            }
        })


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