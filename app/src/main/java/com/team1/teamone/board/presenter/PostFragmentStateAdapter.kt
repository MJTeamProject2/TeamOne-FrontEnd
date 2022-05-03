package com.team1.teamone.board.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.board.view.HomeFragmentList1

class PostFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {


    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return HomeFragmentList1()
    }
}