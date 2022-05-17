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
import com.team1.teamone.board.presenter.WantedBoardAdapter
import com.team1.teamone.board.view.activity.BoardDetailActivity
import com.team1.teamone.board.view.activity.CreateFreeBoardActivity
import com.team1.teamone.databinding.FragmentWantedBoardListBinding
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class WantedBoardListFragment : Fragment() {
    private lateinit var binding : FragmentWantedBoardListBinding
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val boardDataList = mutableListOf<BoardResponse>()
    private lateinit var boardAdapter : WantedBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_wanted_board_list, container, false)
        binding.btnTest.setOnClickListener{
            val intent = Intent(activity, CreateFreeBoardActivity::class.java)
            startActivity(intent)
        }
        drawWantedBoardList()
        return binding.root
    }

    private fun drawWantedBoardList() {
        api.getAllBoards().enqueue(object : Callback<BoardListResponse> {
            override fun onResponse(call: Call<BoardListResponse>, response: Response<BoardListResponse>) {
                Log.d("GET Board ALL", response.toString())
                Log.d("GET Board ALL", response.body().toString())
                Log.d("GET Board ALL33 ", response.body()?.boards.toString())

                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.boards?.let { it1 -> boardDataList.addAll(it1) }

                // 리사이클러뷰 - 어뎁터 연결
                boardAdapter = WantedBoardAdapter(boardDataList)
                binding.rvWantedBoardList.adapter = boardAdapter

                // 리사이클러뷰 보기 형식
                binding.rvWantedBoardList.layoutManager = LinearLayoutManager(
                    this@WantedBoardListFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                boardAdapter.setItemClickListener(object : WantedBoardAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        // 클릭 시 이벤트 작성
                        val intent = Intent(activity, BoardDetailActivity::class.java)
                        intent.putExtra("detailBoardType", boardDataList[position].boardType)
                        intent.putExtra("detailTitle", boardDataList[position].title)
                        intent.putExtra("detailContent", boardDataList[position].content)
                        intent.putExtra("detailViewCount", boardDataList[position].viewCount)
                        intent.putExtra("detailWriter", boardDataList[position].writer?.nickname)
                        intent.putExtra("detailUpdateDate", boardDataList[position].updatedDate)
                        intent.putExtra("detailClassTitle", boardDataList[position].classTitle)
                        intent.putExtra("detailClassDate", boardDataList[position].classDate)
                        intent.putExtra("detailMemberCount", boardDataList[position].memberCount)
                        intent.putExtra("detailDeadline", boardDataList[position].deadLine)
                        startActivity(intent)
                    }
                })
            }
            override fun onFailure(call: Call<BoardListResponse>, t: Throwable) {
                // 실패
                Log.d("GET Board ALL", t.message.toString())
                Log.d("GET Board ALL", "fail")
            }

        })
    }
}