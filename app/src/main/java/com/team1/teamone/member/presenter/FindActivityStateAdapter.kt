package com.team1.teamone.member.presenter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.team1.teamone.member.view.FindIdFragment
import com.team1.teamone.member.view.ResetPasswordFragment

class FindActivityStateAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if(position ==1){
            ResetPasswordFragment()
        }else {
            FindIdFragment()
        }
    }

}