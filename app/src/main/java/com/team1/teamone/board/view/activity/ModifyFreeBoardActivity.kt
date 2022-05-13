package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.FreeBoardRequest
import com.team1.teamone.databinding.ActivityModifyFreeBoardBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_write_free_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ModifyFreeBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var modifyFree: ActivityModifyFreeBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_modify_free_board)

        modifyFree = DataBindingUtil.setContentView(this, R.layout.activity_modify_free_board)
        modifyFree.btnModifyFreeBoard.setOnClickListener{
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val request = FreeBoardRequest(title, content)

            api.putFreeBoard(request).enqueue(object : Callback<BoardResponse> {
                override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>){ //서버에서 보낸 메서드
                    Log.d("auth", RetrofitClient.getAuth())
                    if (response.body() == null ) {
                        Log.d("log", "blank")
                        return
                    } else {
                        val intent = Intent(applicationContext, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                        Log.d("log", "success")
                    }
                }
                override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                    // 실패
                    Log.d("log","fail")
                }
            })
        }
    }
}
