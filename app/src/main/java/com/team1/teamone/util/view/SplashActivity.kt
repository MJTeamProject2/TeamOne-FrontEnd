package com.team1.teamone.util.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.team1.teamone.R
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.member.view.activity.LoginActivity
import com.team1.teamone.member.view.activity.RegisterActivity
import com.team1.teamone.util.network.RetrofitClient
import java.net.CookieManager


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 10000)

    }
}