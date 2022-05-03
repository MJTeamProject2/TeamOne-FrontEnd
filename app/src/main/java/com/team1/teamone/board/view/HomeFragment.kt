package com.team1.teamone.board.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentHomeBinding
import com.team1.teamone.board.presenter.PostFragmentStateAdapter


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    private val tabTitles = arrayListOf("text1", "text22", "text333")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // 네비게이션바 클릭 시

        binding.bookmarkTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_bookMarkFragment)
        }

        binding.messageTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_messageFragment)
        }

        binding.profileTap.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_profileFragment)
        }

        return binding.root
    }


    // 새로 그리는 부분
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val appBar = view.findViewById<AppBarLayout>(R.id.appbar_homeFragment)
        val viewPager2 = view.findViewById<ViewPager2>(R.id.viewpage2_homeFragment)
        val tabLayout = view.findViewById<TabLayout>(R.id.tabLayout_homeFragment)


        viewPager2.adapter =
            PostFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)

        // Tablayout에 문자 추가
        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()

    }
}