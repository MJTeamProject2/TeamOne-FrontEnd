package com.team1.teamone.board.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.board.view.fragment.AppealBoardListFragment
import com.team1.teamone.board.view.fragment.WantedBoardListFragment
import com.team1.teamone.board.view.fragment.FreeBoardListFragment

class PostFragmentStateAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> WantedBoardListFragment()
            1 -> AppealBoardListFragment()
            else -> FreeBoardListFragment()
        }
    }
}