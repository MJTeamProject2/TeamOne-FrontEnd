package com.team1.teamone.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardSimpleModel
import com.team1.teamone.home.presenter.HomeFragementMainAdapter

// HomeFragment 에 보여줄 내용
class HomeFragmentMain : Fragment() {
    private lateinit var homeMainRVAdapter : HomeFragementMainAdapter
    private val boardDataList = mutableListOf<BoardSimpleModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_home_main, container, false)

        boardDataList.add(BoardSimpleModel("제목 1", "같이 팀플할 사람 구해요", "팀프로젝트2", "2022-05-04"))
        boardDataList.add(BoardSimpleModel("제목 2", "같이 팀플할 사람 구해요", "팀프로젝트1", "2022-05-05"))
        boardDataList.add(BoardSimpleModel("제목 3", "같이 팀플할 사람 구해요", "캡스톤디자인1", "2022-05-06"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList.add(BoardSimpleModel("제목 4", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))

        val rvPopularBoard = view.findViewById<RecyclerView>(R.id.rv_popular_board_home_fragment)
        val rvPopularUser = view.findViewById<RecyclerView>(R.id.rv_popular_user_home_fragment)

        homeMainRVAdapter = HomeFragementMainAdapter(boardDataList)
        rvPopularBoard.adapter = homeMainRVAdapter
        rvPopularBoard.layoutManager = LinearLayoutManager(context)

        return view
    }

}