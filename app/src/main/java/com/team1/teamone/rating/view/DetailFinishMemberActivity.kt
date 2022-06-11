package com.team1.teamone.rating.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.*
import com.team1.teamone.databinding.FragmentCreateRatingBinding
import com.team1.teamone.rating.presenter.CreateRatingRVAdapter
import com.team1.teamone.rating.presenter.DetailFinishMemberRVAdapter
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import kotlinx.android.synthetic.main.activity_detail_finish_member.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailFinishMemberActivity : AppCompatActivity() {

    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var detailFinishMemberRVAdapter: DetailFinishMemberRVAdapter
    private val boardMemberDataList = mutableListOf<MemberBoardResponse>()
    lateinit var binding: DetailFinishMemberActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_finish_member)

        // 자신의 ID 가져오기
        val userid = PreferenceUtil.prefs.getString("userid", "")
        Log.d("userid", userid)

        val boardId = intent.getStringExtra("boardId")
        Log.d("boardId", boardId.toString())


        if (boardId != null) {
            postMemberList(userid.toLong(), boardId.toLong())
        }

    }

    private fun postMemberList(memberId: Long, boardId: Long) {
        Log.e("호출", "호출됨")
        api.postFinishMemberList(MemberBoardRequest(memberId, boardId)).enqueue(object :
            Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {
                Log.e("API PostFinishMemberList", response.body().toString())
                // 여기 오류남 고쳐야됨됨
                response.body()?.memberBoardResponseList?.let { it1 -> boardMemberDataList.addAll(it1) }
                // 데이터는 잘 받아오는데 어댑터를 연결하는데서 뭔가 오류가있음
                detailFinishMemberRVAdapter = DetailFinishMemberRVAdapter(boardMemberDataList)
                // ㅇㅋ
                binding.rv_detailFinishMember.adapter = detailFinishMemberRVAdapter
                binding.rv_detailFinishMember.layoutManager = LinearLayoutManager(
                    this@DetailFinishMemberActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }

            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                Log.e("Error API PostFinishMemberList", "error")
            }

        })
    }
}