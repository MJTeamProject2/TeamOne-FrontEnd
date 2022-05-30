package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.AppealBoardRequest
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.ActivityUpdateAppealBoardBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_update_appeal_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UpdateAppealBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var updateAppealBoardBinding: ActivityUpdateAppealBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_appeal_board)

        updateAppealBoardBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_appeal_board)
        updateAppealBoardBinding.btnUpdateAppealBoard.setOnClickListener{
            val title = et_updateAppealBoardTitle.text.toString()
            val classTitle = et_updateAppealBoardClassTitle.text.toString()
            val classDate = et_updateAppealBoardClassDate.text.toString()
            val content = et_updateAppealBoardContent.text.toString()
            val boardId = intent.getLongExtra("updateBoardId", 0)
            val request = AppealBoardRequest(title, classTitle,classDate, content)
            updateAppealBoard(request,boardId)
        }
    }

    private fun updateAppealBoard(request: AppealBoardRequest, boardId: Long) {
        api.putAppealBoard(request, boardId = boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body()?.title.toString() == null) {
                    Log.d("log", "blank")
                    Toast.makeText(this@UpdateAppealBoardActivity, "어필해요 게시물 수정완료", Toast.LENGTH_SHORT).show()
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
