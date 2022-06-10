package com.team1.teamone.rating.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.teamone.R
import com.team1.teamone.profile.presenter.WrittenActivityStateAdapter
import com.team1.teamone.rating.presenter.RatingActivityStateAdapter

class RatingActivity : AppCompatActivity() {
    private val tabTitles = arrayListOf("평가하기", "내가 받은 평가", "내가 한 평가")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val appBar = findViewById<AppBarLayout>(R.id.appbar_rating)
        val viewPager2 = findViewById<ViewPager2>(R.id.viewpage_rating)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_rating)

        viewPager2.adapter =
            RatingActivityStateAdapter(this)

        // Tablayout에 문자 추가
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabTitles[position]

        }.attach()
    }
}