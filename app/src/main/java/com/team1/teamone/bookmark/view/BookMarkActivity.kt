package com.team1.teamone.bookmark.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.view.activity.BoardDetailActivity
import com.team1.teamone.bookmark.model.BookMarkApi
import com.team1.teamone.bookmark.model.BookMarkListResponse
import com.team1.teamone.bookmark.model.BookMarkResponse
import com.team1.teamone.bookmark.presenter.BookMarkAdapter
import com.team1.teamone.databinding.ActivityBookMarkBinding
import com.team1.teamone.util.network.BoolResponse
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.properties.Delegates

class BookMarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookMarkBinding
    private val bookMarkApi = RetrofitClient.create(BookMarkApi::class.java, RetrofitClient.getAuth())
    private val boardApi = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val bookMarkDataList = mutableListOf<BookMarkResponse>()
    private lateinit var bookMarkAdapter : BookMarkAdapter
    private var boardId by Delegates.notNull<Long>()

    // 액티비티 메인
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_mark)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_book_mark)

        // 최초로 들어오면 북마크 리스트 띄우기
        drawBookMarkList()

        // 북마크 전체 삭제
        binding.btnBookmarkRemoveAll.setOnClickListener{
            deleteAllBookMarks() // 전체삭제 확인하는 다이얼로그 출력
        }
    }

    // 북마크 전체 삭제했을때 다이얼로그 띄우기
    private fun deleteAllBookMarks() {
        AlertDialog.Builder(this)
            .setTitle("즐겨찾기 전체 삭제")
            .setMessage("현재 즐겨찾기한 게시물들을 전체 삭제하시겠습니까?")
            .setPositiveButton("예") { dialog, id ->
                deleteRequestToServer() // 서버로 북마크 삭제 요청
            }
            .setNegativeButton("아니오") { dialog, id ->

            }
            .show()
    }

    // 북마크 전체 삭제
    private fun deleteRequestToServer() {
        bookMarkApi.deleteAllBookMarks().enqueue(object : Callback<BoolResponse> {
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                // 성공
                Toast.makeText(this@BookMarkActivity, "즐겨찾기한 게시물 전체 삭제 완료", Toast.LENGTH_SHORT).show()
                // 리스트 다시 보여주기
                drawBookMarkList()
            }
            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("북마크 통신 실패", "북마크 전체 삭제 실패")
            }
        })
    }

    // 북마크 리스트 전체 띄우기
    private fun drawBookMarkList() {
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(call: Call<BookMarkListResponse>, response: Response<BookMarkListResponse>) {
                // 성공
                bookMarkDataList.clear()
                response.body()?.bookMarks?.let { it -> bookMarkDataList.addAll(it) }
                Log.d("북마크 통신 성공", bookMarkDataList.toString())
                bookMarkAdapter = BookMarkAdapter(bookMarkDataList)
                binding.rvBookMark.adapter = bookMarkAdapter
                binding.rvBookMark.layoutManager =
                    LinearLayoutManager(this@BookMarkActivity, LinearLayoutManager.VERTICAL, false)

                // 북마크 상세보기 (게시물 상세보기)
                bookMarkAdapter.setItemClickListener(object : BookMarkAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        boardId = bookMarkDataList[position].board.boardId ?: 0
                        getBoard(boardId)
                        Log.e("position",position.toString())
                        Log.e("boardId", boardId.toString())
                    }
                })
            }

            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.d("북마크 통신 실패", "fail")
            }
        })
    }
    
    // 북마크 아이디로 한개 삭제
    private fun deleteBookMark(bookMarkId : Long) {
        bookMarkApi.deleteBookMark(bookMarkId).enqueue(object : Callback<BoolResponse> {
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                // 성공
                Toast.makeText(this@BookMarkActivity, "즐겨찾기를 해제했습니다.", Toast.LENGTH_SHORT).show()
                drawBookMarkList()
            }
            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("북마크 통신 실패", "북마크 하나 삭제 실패")
            }
        })
    }

    private fun getBoard(boardId : Long) {
        boardApi.getBoard(boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {

                val findBoard = response.body()

                // 클릭 시 이벤트 작성
                val intent = Intent(this@BookMarkActivity, BoardDetailActivity::class.java)
                intent.putExtra("detailBoardId", findBoard?.boardId)
                intent.putExtra("detailBoardType", findBoard?.boardType)
                intent.putExtra("detailTitle", findBoard?.title)
                intent.putExtra("detailContent", findBoard?.content)
                intent.putExtra("detailViewCount", findBoard?.viewCount)
                intent.putExtra("detailWriter", findBoard?.writer?.nickname)
                intent.putExtra("detailUpdateDate", findBoard?.updatedDate)
                intent.putExtra("detailClassTitle", findBoard?.classTitle)
                intent.putExtra("detailClassDate", findBoard?.classDate)
                intent.putExtra("detailMemberCount", findBoard?.memberCount)
                intent.putExtra("detailCurrentMemberCount", findBoard?.currentMemberCount)

                startActivity(intent)
            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                // 실패
                Log.d("GET Board ALL", t.message.toString())
                Log.d("GET Board ALL", "fail")
            }

        })
    }
}