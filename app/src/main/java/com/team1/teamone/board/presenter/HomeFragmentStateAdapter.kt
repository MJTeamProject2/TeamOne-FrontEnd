package com.team1.teamone.board.presenter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.board.view.fragment.HomeFragment
import com.team1.teamone.board.view.HomeFragmentMain

class HomeFragmentStateAdapter(fragmentActivity: HomeFragment) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return HomeFragmentMain()
    }
}