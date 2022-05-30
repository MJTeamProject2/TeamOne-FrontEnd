package com.team1.teamone.board.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.presenter.SearchBoardAdapter
import com.team1.teamone.databinding.ActivitySearchBoardBinding
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var binding: ActivitySearchBoardBinding
    private val boardDataList = mutableListOf<BoardResponse>()
    private lateinit var searchBoardAdapter : SearchBoardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_search_board)

        val keyword : String = intent.getStringExtra("keyword") ?: "키워드"

        val searchWay = intent.getStringExtra("searchWay") ?: "제목"

        val tempSearchWay = when(searchWay) {
            "TITLE" -> "제목으로 검색"
            "CONTENT" -> "내용으로 검색"
            "TITLE_CONTENT" -> "제목+내용으로 검색"
            else -> "수업이름으로 검색"
        }
        binding.tvSearchResult.text = tempSearchWay + "한 " + keyword

//        binding.tvSearchResult.text = ""
        binding.btnBoardSearch2.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                binding.tvSearchResult.text = ""
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 타이핑 중
                binding.tvSearchResult.text = ""
                return true
            }
        })

        drawSearchBoardList(searchWay , keyword)

    }

    private fun drawSearchBoardList(searchWay : String, keyword : String) {
        api.getSearching(searchWay, keyword).enqueue(object : Callback<BoardListResponse> {
            override fun onResponse(call: Call<BoardListResponse>, response: Response<BoardListResponse>) {

                boardDataList.clear()
                // 받아온 리스트 boardDataList 안에 넣기
                response.body()?.boards?.let { it1 -> boardDataList.addAll(it1) }

                if (boardDataList.isEmpty())
                    Toast.makeText(this@SearchBoardActivity, "검색 결과가 없습니다 :(", Toast.LENGTH_SHORT).show()

                // 리사이클러뷰 - 어뎁터 연결
                searchBoardAdapter = SearchBoardAdapter(boardDataList)
                binding.rvSearchBoardList.adapter = searchBoardAdapter

                // 리사이클러뷰 보기 형식
                binding.rvSearchBoardList.layoutManager = LinearLayoutManager(
                    this@SearchBoardActivity.applicationContext,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                searchBoardAdapter.setItemClickListener(object :
                    SearchBoardAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        // 클릭 시 이벤트 작성
                        val intent = Intent(applicationContext, BoardDetailActivity::class.java)
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