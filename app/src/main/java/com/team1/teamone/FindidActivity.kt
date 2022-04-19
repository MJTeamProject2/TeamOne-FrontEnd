package com.team1.teamone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team1.teamone.databinding.ActivityFindidBinding
import androidx.databinding.DataBindingUtil as DataBindingUtil1
import kotlinx.android.synthetic.main.activity_findid.*

class FindidActivity : AppCompatActivity() {

    private lateinit var register2: ActivityFindidBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_findid)
        register2 = DataBindingUtil1.setContentView(this, R.layout.activity_findid)

        register2.btnClosefind.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}