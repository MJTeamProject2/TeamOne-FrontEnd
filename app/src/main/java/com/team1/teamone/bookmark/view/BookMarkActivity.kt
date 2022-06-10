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
import com.team1.teamone.board.presenter.AppealBoardAdapter
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

class BookMarkActivity : AppCompatActivity() {

    private lateinit var binding : ActivityBookMarkBinding
    private val bookMarkApi = RetrofitClient.create(BookMarkApi::class.java, RetrofitClient.getAuth())
    private val boardApi = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val boardDataList = mutableListOf<BoardResponse>()
    private val bookMarkDataList = mutableListOf<BookMarkResponse>()
    private lateinit var bookMarkAdapter : BookMarkAdapter

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
                        getAllBoards()
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

    private fun getAllBoards() {
        boardApi.getAllBoards().enqueue(object : Callback<BoardListResponse> {
            override fun onResponse(call: Call<BoardListResponse>, response: Response<BoardListResponse>) {
                Log.d("GET Board ALL", response.toString())
                Log.d("GET Board ALL", response.body().toString())
                Log.d("GET Board ALL33 ", response.body()?.boards.toString())

                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.boards?.let { it1 -> boardDataList.addAll(it1) }


                bookMarkAdapter.setItemClickListener(object :
                    BookMarkAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        // 클릭 시 이벤트 작성
                        val intent = Intent(this@BookMarkActivity, BoardDetailActivity::class.java)
                        intent.putExtra("detailBoardId", boardDataList[position].boardId)
                        intent.putExtra("detailBoardType", boardDataList[position].boardType)
                        intent.putExtra("detailTitle", boardDataList[position].title)
                        intent.putExtra("detailContent", boardDataList[position].content)
                        intent.putExtra("detailViewCount", boardDataList[position].viewCount)
                        intent.putExtra("detailWriter", boardDataList[position].writer?.nickname)
                        intent.putExtra("detailUpdateDate", boardDataList[position].updatedDate)
                        intent.putExtra("detailClassTitle", boardDataList[position].classTitle)
                        intent.putExtra("detailClassDate", boardDataList[position].classDate)

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