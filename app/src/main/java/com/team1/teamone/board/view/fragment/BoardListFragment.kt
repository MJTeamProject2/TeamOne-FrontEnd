package com.team1.teamone.board.view.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.FragmentHomeList1Binding
import com.team1.teamone.board.presenter.BoardAdapter
import com.team1.teamone.board.view.activity.WriteFreeBoardActivity
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class BoardListFragment : Fragment() {
    private lateinit var binding : FragmentHomeList1Binding
//    val api = RetrofitService.create()
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val boardDataList = mutableListOf<BoardResponse>()
    private lateinit var boardRVAdapter : BoardAdapter

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



        binding.btnGettest.setOnClickListener {
            api.getAllBoards().enqueue(object : Callback<BoardListResponse> {
                override fun onResponse(
                    call: Call<BoardListResponse>,
                    response: Response<BoardListResponse>
                ) {
                    Toast.makeText(context, "연결 성공.", Toast.LENGTH_SHORT).show()
                    Log.d("GET Board ALL",response.toString())
                    Log.d("GET Board ALL",response.body().toString())
                    Log.d("GET Board ALL33 ",response.body()?.boards.toString())

                    // 받아온 리스트 boardDataList 안에 넣기
                    response.body()?.boards?.let { it1 -> boardDataList.addAll(it1) }

                    // 리사이클러뷰 - 어뎁터 연결
                    boardRVAdapter = BoardAdapter(boardDataList)
                    binding.rvProfile.adapter = boardRVAdapter

                    // 리사이클러뷰 보기 형식
                    binding.rvProfile.layoutManager = LinearLayoutManager(this@BoardListFragment.context, LinearLayoutManager.VERTICAL, false)

                }

                override fun onFailure(call: Call<BoardListResponse>, t: Throwable) {
                    // 실패
                    Log.d("GET Board ALL",t.message.toString())
                    Log.d("GET Board ALL","fail")
                }

            })
        }
//        api.getBoard(3).enqueue(object : Callback<BoardResponse> {
//
//            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
////                print("tlqkf")
////                Log.d("response", response.message())
////                val boards = response.body()?.boards
////                Log.d("size", boardList.size.toString())
////                //for (i in boards.orEmpty()) {
////                    Log.d("dmd",i.title.toString())
//////                    profileList.add(Profiles(R.drawable.ic_baseline_bookmark_24, i.title.toString()+"1", 28, "안드로이드 앱 개발자"))
////                    boardList.add(boards[0])
////                //}
////                val board = response.body()
////                if (board != null) {
////                    boardList.add(board)
////                }
//
////                binding.rvProfile.layoutManager = LinearLayoutManager(this@HomeFragmentList1.context, LinearLayoutManager.VERTICAL, false)
////                binding.rvProfile.setHasFixedSize(true)
////                binding.rvProfile.adapter = BoardAdapter(boardList)
//
//            }
//
//
//            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
//
//            }
//        })


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