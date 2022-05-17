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
import com.team1.teamone.board.presenter.AppealBoardAdapter
import com.team1.teamone.board.presenter.FreeBoardAdapter
import com.team1.teamone.board.view.activity.BoardDetailActivity
import com.team1.teamone.board.view.activity.CreateAppealBoardActivity
import com.team1.teamone.board.view.activity.CreateFreeBoardActivity
import com.team1.teamone.databinding.FragmentAppealBoardListBinding
import com.team1.teamone.databinding.FragmentFreeBoardListBinding
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AppealBoardListFragment : Fragment() {
    private lateinit var binding : FragmentAppealBoardListBinding
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val boardDataList = mutableListOf<BoardResponse>()
    private lateinit var appealBoardAdapter : AppealBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_appeal_board_list, container, false)
        binding.btnTest.setOnClickListener{
            val intent = Intent(activity, CreateAppealBoardActivity::class.java)
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
                    appealBoardAdapter = AppealBoardAdapter(boardDataList)
                    binding.rvAppealBoardList.adapter = appealBoardAdapter

                    // 리사이클러뷰 보기 형식
                    binding.rvAppealBoardList.layoutManager = LinearLayoutManager(this@AppealBoardListFragment.context, LinearLayoutManager.VERTICAL, false)

                    appealBoardAdapter.setItemClickListener(object: AppealBoardAdapter.OnItemClickListener{
                        override fun onClick(v: View, position: Int) {
                            // 클릭 시 이벤트 작성
                            val intent = Intent(getActivity(), BoardDetailActivity::class.java)
                            intent.putExtra("detailTitle",boardDataList[position].updateDate)
                            intent.putExtra("detailContent",boardDataList[position].content)
                            //intent.putExtra("detailViewCount",boardDataList[position].viewCount)
                            //intent.putExtra("detailBoardType",boardDataList[position].boardType)
                            intent.putExtra("detailMemberCount",boardDataList[position].memberCount)
                            intent.putExtra("detailClassTitle",boardDataList[position].classTitle)
                            intent.putExtra("detailClassDate",boardDataList[position].classDate)
//                            intent.putExtra("detail",boardDataList[position].deadLine)
//                            intent.putExtra("detail",boardDataList[position].createdAt)
//                            intent.putExtra("detail",boardDataList[position].boardStatus)
//                            intent.putExtra("detail",boardDataList[position].comments)
                            startActivity(intent)
                        }
                    })
                }

                override fun onFailure(call: Call<BoardListResponse>, t: Throwable) {
                    // 실패
                    Log.d("GET Board ALL",t.message.toString())
                    Log.d("GET Board ALL","fail")
                }

            })
        }

        return binding.root
    }
}