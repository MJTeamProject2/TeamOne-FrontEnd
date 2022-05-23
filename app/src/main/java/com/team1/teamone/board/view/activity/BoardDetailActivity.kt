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

        binding.detailTitle.text = title
        binding.tvDetailContent.text = content
        //.tvDetailViewCount.text = viewCount.toString()
        binding.tvUpdateDate.text = updateDate
        binding.tvDetailWriter.text = writer
        Log.e("boardType",boardType.toString())
        // 게시글 수정
        if(boardType.toString() == "FREE") {
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
                intent.putExtra("updateBoardId", boardId)
                startActivity(intent)
            }
        }

        if(boardType.toString() == "APPEAL") {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            binding.tvDetailClassTitle.text = classTitle
            binding.tvDetailClassDate.text = classDate

            // 게시글 수정
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateAppealBoardActivity::class.java)
                intent.putExtra("updateBoardId",boardId)
                startActivity(intent)
            }
        }

        if(boardType.toString() == "WANTED") {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            val memberCount = intent.getStringExtra("detailMemberCount")
            val deadLine = intent.getStringExtra("detailDeadline")
            binding.tvDetailMemberCount.text = memberCount
            binding.tvDetailDeadline.text = deadLine
            binding.tvDetailClassDate.text = classDate
            binding.tvDetailClassTitle.text = classTitle
            // 게시글 수정
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateWantedBoardActivity::class.java)
                intent.putExtra("updateBoardId",boardId)
                startActivity(intent)
            }
        }

        drawCommentList(boardId)
        updateBookMarkStar(boardId)

        // 게시글 삭제 버튼
        binding.btnBoardDetailDeleteBoard.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)

            startActivity(intent)
        }


        // 북마크 (즐겨찾기 등록) 버튼
        binding.btnBoardDetailBookMark.setOnClickListener {
            toggleBookMark(boardId)
        }

    }

    /**
     *  북마크 관련 로직
     */
    // 처음 게시글 상세 페이지를 켤때, 북마크로 된 게시글인지 아닌지를 검사해서 보여주기
    private fun updateBookMarkStar(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                Log.e("업데이트 북마크 호출", "성공")
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // 가져온 북마크 리스트에 해당 게시물이 있는 지 확인
                for (bookMark in bookMarkList) {
                    // 있으면 꽉찬 스타
                    if (bookMark.board.boardId == boardId) {
                        binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                        return
                    }
                }
                // 없으면 비어있는 스타
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 리스트 조회 실패", "실패")
            }

        })
    }

    // 북마크 등록
    private fun postBookMark(boardId: Long) {
        bookMarkApi.createBookMark(boardId).enqueue(object : Callback<BookMarkResponse> {
            override fun onResponse(
                call: Call<BookMarkResponse>,
                response: Response<BookMarkResponse>
            ) {
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기에 등록했습니다", Toast.LENGTH_SHORT).show()
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
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기 목록에서 삭제했습니다", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 삭제 실패", "실패")
            }
        })
    }

    // 북마크 토글
    private fun toggleBookMark(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // 가져온 북마크 리스트에 해당 게시물이 있는 지 확인
                for (bookMark in bookMarkList) {
                    // 있으면 북마크 해제
                    if (bookMark.board.boardId == boardId) {
                        deleteBookMark(bookMark.bookMarkId)
                        return
                    }
                }
                // 없으면 북마크 등록
                postBookMark(boardId)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 리스트 조회 실패", "실패")
            }

        })
    }

    // 게시판 그리기
    private fun drawCommentList(boardId: Long) {
        boardApi.getAllComments(boardId).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.comments?.let { it -> commentDataList.addAll(it) }

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