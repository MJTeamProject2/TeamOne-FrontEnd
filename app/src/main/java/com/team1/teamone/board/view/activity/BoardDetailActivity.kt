package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.*
import com.team1.teamone.board.presenter.CommentAdapter
import com.team1.teamone.databinding.ActivityBoardDetailBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val commentDataList = mutableListOf<CommentResponse>()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var detail: ActivityBoardDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val boardType = intent.getStringExtra("detailBoardType")
        val title = intent.getStringExtra("detailTitle")
        val content = intent.getStringExtra("detailContent")
       // val viewCount = intent.getIntExtra("detailViewCount")?.toInt()
        val writer = intent.getStringExtra("detailWriter")
        val updateDate = intent.getStringExtra("detailUpdateDate")
        val boardId = intent.getLongExtra("detailBoardId",0)

        detail = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        if(boardType != "free" ) {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            detail.tvDetailClassTitle.text = classTitle
            detail.tvDetailClassTime.text = classDate

        }

        if(boardType == "wanted") {
            val memberCount = intent.getStringExtra("detailMemberCount")
            detail.tvDetailMemberCount.text = memberCount
            val deadLine = intent.getStringExtra("detailDeadline")
            detail.tvDetailDeadline.text = deadLine

        }

        detail.detailTitle.text = title
        detail.tvDetailContent.text = content
        //.tvDetailViewCount.text = viewCount.toString()
        detail.tvUpdateDate.text = updateDate
        detail.tvDetailWriter.text = writer
        Log.e("배성",boardId.toString())

        if (boardId != null) {
            Log.e("gimozzi","1")
            drawCommentList(boardId)
            Log.e("ang","2")
        }

        detail.imageButton5.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        detail.imageButton7.setOnClickListener {
            val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    private fun drawCommentList(boardId: Long) {
        Log.e("tlqkf","bae")
        api.getAllComments(boardId).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.comments?.let { it1 -> commentDataList.addAll(it1) }

                // 리사이클러뷰 - 어뎁터 연결
                commentAdapter = CommentAdapter(commentDataList)
                detail.rvCommentList.adapter = commentAdapter

                // 리사이클러뷰 보기 형식
                detail.rvCommentList.layoutManager = LinearLayoutManager(
                    this@BoardDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }

            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                // 실패
                Log.d("유의 통신 실패", "유의 전체 조회 실패")
            }
        })
    }
}