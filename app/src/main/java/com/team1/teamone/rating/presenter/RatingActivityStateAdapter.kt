package com.team1.teamone.rating.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.profile.view.WrittenAppealBoardListFragment
import com.team1.teamone.profile.view.WrittenFreeBoardListFragment
import com.team1.teamone.profile.view.WrittenWantedBoardListFragment
import com.team1.teamone.rating.view.CreateRatingFragment
import com.team1.teamone.rating.view.RatedListFragment
import com.team1.teamone.rating.view.RatingFragment
import com.team1.teamone.rating.view.RatingListFragment

class RatingActivityStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> CreateRatingFragment()
            1 -> RatedListFragment()
            else -> RatingListFragment()
        }
    }
}