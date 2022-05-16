package com.team1.teamone.profile.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.teamone.R
import com.team1.teamone.board.presenter.PostFragmentStateAdapter
import com.team1.teamone.profile.presenter.WrittenActivityStateAdapter

class WrittenBoardActivity : AppCompatActivity() {
    private val tabTitles = arrayListOf("팀원 구해요", "어필해요", "자유 게시판")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_written_board)

        val appBar = findViewById<AppBarLayout>(R.id.appbar_homeFragment)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewpage2_homeFragment)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_homeFragment)

        viewPager2.adapter =
            WrittenActivityStateAdapter(this)

        // Tablayout에 문자 추가
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabTitles[position]

        }.attach()
    }
}