package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.WantedBoardRequest
import com.team1.teamone.databinding.ActivityCreateWantedBoardBinding
import com.team1.teamone.databinding.ActivityUpdateWantedBoardBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.util.network.RetrofitClient
import kotlinx.android.synthetic.main.activity_create_wanted_board.*
import kotlinx.android.synthetic.main.activity_update_wanted_board.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern


class CreateWantedBoardActivity : AppCompatActivity() {
    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private lateinit var binding: ActivityCreateWantedBoardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_wanted_board)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_create_wanted_board)

        binding.edtCreateWantedBoardMemberCount.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            val memberCountCheck: Pattern = Pattern.compile("""^[0-9]+$""")
            if (source == "" || memberCountCheck.matcher(source).matches()) {
                return@InputFilter source
            }
            Toast.makeText( this, "숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show()
            ""
        }, InputFilter.LengthFilter(3))


        binding.btnWriteRecruitmentBoard.setOnClickListener{
            val title = edt_create_wanted_board_title.text.toString()
            val memberCount = edt_create_wanted_board_memberCount.text.toString()
            val classTitle = edt_create_wanted_board_classTitle.text.toString()
            val classDate = edt_create_wanted_board_classDate.text.toString()
            val content = edt_create_wanted_board_content.text.toString()
            val request = WantedBoardRequest(title, memberCount, classTitle,classDate, content)
            createWantedBoard(request)
        }
    }


    private fun createWantedBoard(request: WantedBoardRequest) {
        api.postWantedBoard(request).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body()?.title.toString() == null) {
                    Toast.makeText(this@CreateWantedBoardActivity, "팀원구해요 게시물 작성 완료", Toast.LENGTH_SHORT).show()
                    Log.d("log", "blank")
                    return
                } else {
                    val intent = Intent(applicationContext, HomeActivity::class.java)
                    startActivity(intent)
                    Log.e("title", request.classTitle)
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
