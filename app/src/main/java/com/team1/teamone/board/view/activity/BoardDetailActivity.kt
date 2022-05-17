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

        val boardType = intent.getStringExtra("detailBoardType")
        val title = intent.getStringExtra("detailTitle")
        val content = intent.getStringExtra("detailContent")
        val viewCount = intent.getStringExtra("detailViewCount")
        val writer = intent.getStringExtra("detailWriter")
        val updateDate = intent.getStringExtra("detailUpdateDate")

        detail = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        if(boardType != "free" ) {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            detail.tvDetailClassTitle.text = classTitle
            detail.tvDetailClassTime.text = classDate
        }

        if(boardType == "wanted") {
            val memberCount = intent.getStringExtra("detailMemberCount")
            detail.tvDetailMemberCount.text = memberCount
            val deadLine = intent.getStringExtra("detailDeadline")
            detail.tvDetailDeadline.text = deadLine
        }

        detail.detailTitle.text = title
        detail.tvDetailContent.text = content
        detail.tvDetailViewCount.text = viewCount
        detail.tvUpdateDate.text = updateDate
        detail.tvDetailWriter.text = writer

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