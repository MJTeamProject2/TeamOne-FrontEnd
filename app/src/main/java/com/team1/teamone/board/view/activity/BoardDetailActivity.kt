package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.*
import com.team1.teamone.board.presenter.CommentAdapter
import com.team1.teamone.bookmark.model.BookMarkApi
import com.team1.teamone.bookmark.model.BookMarkListResponse
import com.team1.teamone.bookmark.model.BookMarkResponse
import com.team1.teamone.databinding.ActivityBoardDetailBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.BoolResponse
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    private val boardApi = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val bookMarkApi = RetrofitClient.create(BookMarkApi::class.java, RetrofitClient.getAuth())
    private val commentDataList = mutableListOf<CommentResponse>()
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var binding: ActivityBoardDetailBinding

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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        if(boardType != "free" ) {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            binding.tvDetailClassTitle.text = classTitle
            binding.tvDetailClassTime.text = classDate

        }

        if(boardType == "wanted") {
            val memberCount = intent.getStringExtra("detailMemberCount")
            binding.tvDetailMemberCount.text = memberCount
            val deadLine = intent.getStringExtra("detailDeadline")
            binding.tvDetailDeadline.text = deadLine
        }

        binding.detailTitle.text = title
        binding.tvDetailContent.text = content
        //.tvDetailViewCount.text = viewCount.toString()
        binding.tvUpdateDate.text = updateDate
        binding.tvDetailWriter.text = writer
        Log.e("배성",boardId.toString())

        if (boardId != null) {
            Log.e("gimozzi","1")
            drawCommentList(boardId)
            Log.e("ang","2")
        }

        // 게시글 삭제
        binding.btnBoardDetailDeleteBoard.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 게시글 수정
        binding.btnBoardDetailUpdateBoard.setOnClickListener {
            val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
            startActivity(intent)
            finish()
        }

        // 북마크 (즐겨찾기 등록)
        binding.btnBoardDetailBookMark.setOnClickListener {
            // 서버로부터 로그인된 회원의 북마크 리스트를 받아온다
            val userBookMarkList = getUserBookMarkList()

            // 순회를 돌면서 서버로부터 가져온 북마크 리스트에 해당 게시글이 포함되어있는지 확인
            userBookMarkList.forEach{bookMarkResponse ->
                // 이미 즐겨찾기에 등록된 게시글이면 북마크 삭제
                if (bookMarkResponse.board.boardId == boardId) deleteBookMark(bookMarkResponse.bookMarkId)
                // 아니면 북마크 생성(등록)
                else postBookMark(boardId)
            }
        }

    }

    // 북마크 등록
    private fun postBookMark(boardId: Long) {
        bookMarkApi.createBookMark(boardId).enqueue(object : Callback<BookMarkResponse> {
            override fun onResponse(
                call: Call<BookMarkResponse>,
                response: Response<BookMarkResponse>
            ) {
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기에 등록했습니다", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<BookMarkResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 생성 실패", "실패")
            }
        })
    }

    // 북마크 삭제
    private fun deleteBookMark(bookMarkId: Long) {
        bookMarkApi.deleteBookMark(bookMarkId).enqueue(object : Callback<BoolResponse>{
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기 목록에서 삭제했습니다", Toast.LENGTH_LONG).show()
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 삭제 실패", "실패")
            }
        })
    }

    // 일단 로그인된 사용자의 북마크 리스트를 전체 받아온다
    private fun getUserBookMarkList(): MutableList<BookMarkResponse> {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
            }

            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 리스트 조회 실패", "실패")
            }

        })
        return bookMarkList
    }

    // 게시판 그리기
    private fun drawCommentList(boardId: Long) {
        boardApi.getAllComments(boardId).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.comments?.let { it1 -> commentDataList.addAll(it1) }

                // 리사이클러뷰 - 어뎁터 연결
                commentAdapter = CommentAdapter(commentDataList)
                binding.rvCommentList.adapter = commentAdapter

                // 리사이클러뷰 보기 형식
                binding.rvCommentList.layoutManager = LinearLayoutManager(
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