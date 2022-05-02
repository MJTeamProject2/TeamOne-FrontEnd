package com.team1.teamone.loading

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.team1.teamone.MainActivity
import com.team1.teamone.R
import com.team1.teamone.auth.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        Handler().postDelayed({
            val Intent = Intent(this, MainActivity::class.java)
            startActivity(Intent)
            finish()
        }, 3000)

    }
}