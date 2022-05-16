package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.databinding.ActivityBoardDetailBinding
import com.team1.teamone.home.view.HomeActivity

class BoardDetailActivity : AppCompatActivity() {

    private lateinit var detail: ActivityBoardDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val title = intent.getStringExtra("detailTitle")
        val classTitle = intent.getStringExtra("detailClassTitle")
        val classTime = intent.getStringExtra("detailClassTime")
        val content = intent.getStringExtra("detailContent")
        val memberCount = intent.getStringExtra("detailMemberCount")

        detail = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)
        detail.detailTitle.text = title
        detail.tvDetailClassTitle.text = classTitle
        detail.tvDetailClassTime.text = classTime
        detail.tvDetailContent.text = content
        detail.tvDetailMemberCount.text = memberCount

        detail.imageButton5.setOnClickListener{
            val intent = Intent(applicationContext, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        detail.imageButton7.setOnClickListener {
            val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

}