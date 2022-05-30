package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.AppealBoardRequest
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.ActivityCreateAppealBoardBinding
import com.team1.teamone.databinding.ActivityUpdateAppealBoardBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_appeal_board.*
import kotlinx.android.synthetic.main.activity_update_appeal_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateAppealBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var binding: ActivityCreateAppealBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_appeal_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_appeal_board)
        binding.btnWriteAppealBoard.setOnClickListener{
            val title = edt_create_appeal_board_title.text.toString()
            val classTitle = edt_create_appeal_board_className.text.toString()
            val classDate = edt_create_appeal_board_classTime.text.toString()
            val content = edt_create_appeal_board_content.text.toString()
            val request = AppealBoardRequest(title, classTitle,classDate, content)
            createAppealBoard(request)
        }
    }

    private fun createAppealBoard(request: AppealBoardRequest) {
        api.postAppealBoard(request).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body()?.title.toString() == null) {
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
