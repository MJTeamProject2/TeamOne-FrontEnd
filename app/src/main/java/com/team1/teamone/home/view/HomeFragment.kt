package com.team1.teamone.home.view

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import com.team1.teamone.R
import com.team1.teamone.board.view.activity.SearchBoardActivity
import com.team1.teamone.board.view.activity.UpdateWantedBoardActivity
import com.team1.teamone.databinding.FragmentHomeBinding
import com.team1.teamone.home.presenter.HomeFragmentStateAdapter
import com.team1.teamone.member.model.MemberApi
import com.team1.teamone.util.network.RetrofitClient


class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding
    val api = RetrofitClient.create(MemberApi::class.java)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val searchList = listOf("TITLE", "CONTENT", "TITLE_CONTENT", "CLASS")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        // viewpager2 연결
        binding.viewPager2HomeFragement.adapter = HomeFragmentStateAdapter(this)

        var keyword = ""
        var searchWay = ""
        binding.usernameHome.text = "조기천"





        // 검색 타입 설정 (드롭다운)
        binding.dropdownSearchType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                searchWay = searchList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        // 검색
        binding.btnBoardSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                keyword = binding.btnBoardSearch.query.toString()
                val intent = Intent(context, SearchBoardActivity::class.java)
                intent.putExtra("searchWay", searchWay)
                intent.putExtra("keyword", keyword)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // 타이핑 중
                return true
            }
        })

        return binding.root
    }

}