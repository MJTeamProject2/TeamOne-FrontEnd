package com.team1.teamone.profile.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.profile.view.WrittenAppealBoardListFragment
import com.team1.teamone.profile.view.WrittenWantedBoardListFragment
import com.team1.teamone.profile.view.WrittenFreeBoardListFragment

class WrittenActivityStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> WrittenWantedBoardListFragment()
            1 -> WrittenAppealBoardListFragment()
            else -> WrittenFreeBoardListFragment()
        }
    }
}