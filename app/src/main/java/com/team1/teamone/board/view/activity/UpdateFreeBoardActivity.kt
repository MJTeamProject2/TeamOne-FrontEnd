package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.FreeBoardRequest
import com.team1.teamone.databinding.ActivityUpdateFreeBoardBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_update_free_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpdateFreeBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var updateFreeBoardBinding: ActivityUpdateFreeBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_free_board)

        updateFreeBoardBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_free_board)
        updateFreeBoardBinding.btnModifyFreeBoard.setOnClickListener{
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val boardId = intent.getLongExtra("updateBoardId", 0)
            val request = FreeBoardRequest(title, content)
            updateFreeBoard(request, boardId)
        }
    }

    private fun updateFreeBoard(request: FreeBoardRequest, boardId: Long) {
        api.putFreeBoard(request,boardId = boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body()?.title.toString() == null) {
                    Toast.makeText(this@UpdateFreeBoardActivity, "자유 게시물 수정완료", Toast.LENGTH_SHORT).show()
                    Log.d("log", "blank")
                    return
                } else {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    Log.d("log", "success")
                }
            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                // 실패
                Log.d("log", "fail")
            }
        })
    }
}
