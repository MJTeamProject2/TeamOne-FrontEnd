package com.team1.teamone.board.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.databinding.ActivityWriteFreeBoardBinding
import com.team1.teamone.network.*
import kotlinx.android.synthetic.main.activity_write_free_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WriteFreeBoardActivity : AppCompatActivity() {
    private val api = RetrofitService.create(RetrofitService.getAuth())
    private lateinit var register: ActivityWriteFreeBoardBinding
    private var TAG: String = "Register"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_write_free_board)
        var isTitleBlank = false
        var isContentBlank = false

        register = DataBindingUtil.setContentView(this, R.layout.activity_write_free_board)
        register.btnWriteFreeBoard.setOnClickListener{
            val title = et_title.text.toString()
            val content = et_content.text.toString()
            val request = FreeBoardRequest(title, content)

            api.postFreeBoard(request).enqueue(object : Callback<BoardResponse> {
                override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>){ //서버에서 보낸 메서드
                    Log.d("auth", RetrofitService.getAuth())
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

