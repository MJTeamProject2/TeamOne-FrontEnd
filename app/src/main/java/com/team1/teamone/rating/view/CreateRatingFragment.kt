package com.team1.teamone.rating.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.MemberBoardListResponse
import com.team1.teamone.board.model.MemberBoardResponse
import com.team1.teamone.board.view.activity.SearchBoardActivity
import com.team1.teamone.databinding.FragmentCreateRatingBinding
import com.team1.teamone.rating.presenter.CreateRatingRVAdapter
import com.team1.teamone.rating.presenter.RatingAdapter
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRatingFragment : Fragment() {

    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var createRatingRVAdapter: CreateRatingRVAdapter
    private val boardMemberDataList = mutableListOf<MemberBoardResponse>()
    lateinit var binding : FragmentCreateRatingBinding

    val userid = PreferenceUtil.prefs.getString("userid",  "")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        finishList()

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_create_rating, container, false)
        return binding.root
    }

    private fun finishList() {
        // 종료된 가입된 목록 가져 오기
        api.getFinishBoardList(userid.toLong()).enqueue(object :
            Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {
                Log.d("API GetFinishBoardList",response.body().toString())

                response.body()?.memberBoardResponseList?.let { it1 -> boardMemberDataList.addAll(it1) }
                createRatingRVAdapter = CreateRatingRVAdapter(boardMemberDataList)
                binding.rvCreatedRating.adapter = createRatingRVAdapter
                binding.rvCreatedRating.layoutManager = LinearLayoutManager(
                    this@CreateRatingFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                // 클릭 시 평가 할 수 있는 맴버들 보여줌
                createRatingRVAdapter.setItemClickListener(object :
                    CreateRatingRVAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        val intent = Intent(context, DetailFinishMemberActivity::class.java)
                        Log.e("boardId receive CreateRating",  boardMemberDataList[position].board?.boardId.toString())
                        intent.putExtra("boardId", boardMemberDataList[position].board?.boardId.toString())
                      //  intent.putExtra("memberBoardId", boardMemberDataList[position].memberBoardId)
                        startActivity(intent)
                    }
                })

            }

            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                Log.e("Error API GetFinishBoardList", "error")
            }

        })
    }
}