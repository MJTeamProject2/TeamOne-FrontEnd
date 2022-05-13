package com.team1.teamone.profile.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.bookmark.view.BookMarkActivity
import com.team1.teamone.databinding.FragmentProfileBinding
import com.team1.teamone.home.view.HomeActivity

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_profile, container, false)

        binding.btnProfileEdit.setOnClickListener {
            val intent = Intent(context, ProfileDetailActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }
}