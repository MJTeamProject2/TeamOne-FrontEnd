package com.team1.teamone.board.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentBookMarkBinding


class BookMarkFragment : Fragment() {

    private lateinit var binding : FragmentBookMarkBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_book_mark, container, false)

        // 네비게이션바 클릭 시

        binding.homeTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookMarkFragment_to_homeFragment)
        }

        binding.messageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookMarkFragment_to_messageFragment)
        }

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_bookMarkFragment_to_profileFragment)
        }

        return binding.root
    }

}