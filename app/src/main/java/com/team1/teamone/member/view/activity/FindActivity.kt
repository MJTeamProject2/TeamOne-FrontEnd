package com.team1.teamone.member.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.teamone.R
import com.team1.teamone.member.presenter.FindActivityStateAdapter
import kotlinx.android.synthetic.main.activity_find.*
import kotlin.math.abs

class FindActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find)

        val viewPager2 = findViewById<ViewPager2>(R.id.viewPager2_find)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout_find)
        val appbar = findViewById<AppBarLayout>(R.id.appbar_find)

        // 뷰 페이지
        viewPager2.adapter = FindActivityStateAdapter(this)

        TabLayoutMediator(tabLayout, viewPager2){tab, position ->
            if(position == 1){
                tab.text = "비밀번호 찾기"
            }else{
                tab.text = "아이디 찾기"
            }
        }.attach()

        var initTransitionY = tabLayout.translationY
        tabLayout.post {
            initTransitionY = tabLayout.translationY
        }

        appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

            //Check if the view is collapsed
            if (abs(verticalOffset) >= appbar.totalScrollRange) {
                collapsingToolbar.title = ""
            } else {
                collapsingToolbar.title = ""
            }

            tabLayout.translationY =
                initTransitionY + initTransitionY * (verticalOffset / appBarLayout.totalScrollRange.toFloat())

        })
    }
}