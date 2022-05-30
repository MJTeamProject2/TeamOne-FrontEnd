package com.team1.teamone.board.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.team1.teamone.R
import com.team1.teamone.board.presenter.PostFragmentStateAdapter
import com.team1.teamone.board.view.activity.CreateFreeBoardActivity
import com.team1.teamone.databinding.FragmentBoardBinding


class BoardFragment : Fragment() {

    private lateinit var binding : FragmentBoardBinding
    private val tabTitles = arrayListOf("팀원 구해요", "어필해요", "자유 게시판")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_board, container, false)



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