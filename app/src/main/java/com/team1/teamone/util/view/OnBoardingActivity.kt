package com.team1.teamone.util.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.team1.teamone.R
import com.team1.teamone.member.view.LoginActivity
import com.team1.teamone.member.view.RegisterActivity
import com.team1.teamone.util.model.OnBoardingData
import com.team1.teamone.util.presentation.OnBoardViewPagerAdapter

class OnBoardingActivity : AppCompatActivity() {

    var onBoardingViewPagerAdapter : OnBoardViewPagerAdapter? = null
//    var tabLayout : TabLayout? = null
    var tabLayout : DotsIndicator? = null
    var onBoardingViewPager : ViewPager? = null
    var next: TextView? = null
    var position = 0
    var sharedPreferences : SharedPreferences? = null
    var startOnboardBtn : Button? = null
    var loginOnboard : LinearLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (restorePrefData()) {
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }

        setContentView(R.layout.activity_on_boarding)

        tabLayout = findViewById(R.id.dots_indicator)
        next = findViewById(R.id.tv_next_onboard)
        startOnboardBtn = findViewById(R.id.btn_startOnboard)
        loginOnboard = findViewById(R.id.onboard_login)

        val onBoardingData : MutableList<OnBoardingData> = ArrayList()
        onBoardingData.add(OnBoardingData("팀원은 어디서 구하지?", "이번 학기에도 팀플이 있는 당신.. \n 아직도 팀원을 못 구했나요? \n 같이 수업을 듣는 사람이 없다고요?", R.drawable.solo1, "조별과제 / 팀플"))
        onBoardingData.add(OnBoardingData("원하는 팀원을 빠르게!", "같은 수업을 듣고 있는 사람을 찾아봐요! \n 게시판 별로 팀원을 찾을 수도 있고 \n 자기 자신을 어필할 수 있습니다!", R.drawable.board2, "팀원 찾기를 통해"))
        onBoardingData.add(OnBoardingData("팀원을 구하세요!", "팀원들의 평가를 한 눈에 보고 \n 자신과 어울리는 팀원을 찾으세요! \n 팀플이 종료 되면 평가도 남길 수 있어요!", R.drawable.review2, "자신과 어울리는"))
        onBoardingData.add(OnBoardingData("성공적인 팀플을 위하여", "더 이상 팀원 때문에 고민 하지 말고 \n TeamOne 어플을 통해 \n 손 쉽게 팀원을 구하세요!", R.drawable.team1, "A+를 위하여"))

        setOnBoardingViewPagerAdapter(onBoardingData)

        position = onBoardingViewPager!!.currentItem

        startOnboardBtn?.setOnClickListener {
            savePrefDate()
            val i = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        loginOnboard?.setOnClickListener {
            savePrefDate()
            val i = Intent(applicationContext, LoginActivity::class.java)
            startActivity(i)
            finish()
        }


//        next?.setOnClickListener {
//            if (position < onBoardingData.size) {
//                position++
//                onBoardingViewPager!!.currentItem = position
//            }
//            if (position == onBoardingData.size) {
//                savePrefDate()
//                val i = Intent(applicationContext, LoginActivity::class.java)
//                startActivity(i)
//                finish()
//            }
//        }

//        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
//            override fun onTabSelected(tab: TabLayout.Tab?) {
////                position = tab!!.position
////                if (tab.position == onBoardingData.size - 1) {
////                    next!!.text = "Get Started"
////                } else {
////                    next!!.text = "Next"
////                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//
//            }
//        })
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>){

        onBoardingViewPager = findViewById(R.id.screenPager)
        onBoardingViewPagerAdapter = OnBoardViewPagerAdapter(this,onBoardingData)
        onBoardingViewPager!!.adapter = onBoardingViewPagerAdapter
        tabLayout?.attachTo(onBoardingViewPager!!)
    }

    private fun savePrefDate() {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        val editor = sharedPreferences!!.edit()
        editor.putBoolean("isFirstTimeRun", true)
        editor.apply()
    }

    private fun restorePrefData(): Boolean {
        sharedPreferences = applicationContext.getSharedPreferences("pref", Context.MODE_PRIVATE)
        return sharedPreferences!!.getBoolean("isFirstTimeRun", false)
    }
}