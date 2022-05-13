package com.team1.teamone.home.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.home.model.BoardSimpleModel
import com.team1.teamone.home.model.UserSimpleModel
import com.team1.teamone.home.presenter.HomeFragementMainAdapter
import com.team1.teamone.home.presenter.HomeFragmentMainNewBoardAdapter
import com.team1.teamone.home.presenter.HomeFragmentMainUserAdapter

// HomeFragment 에 보여줄 내용
class HomeFragmentMain : Fragment() {
    private lateinit var homeMainRVAdapter : HomeFragementMainAdapter
    private lateinit var homeMainRVAdapter2 : HomeFragmentMainUserAdapter
    private lateinit var homeMainRVAdapter3 : HomeFragmentMainNewBoardAdapter
    private val boardDataList = mutableListOf<BoardSimpleModel>()
    private val boardDataList2 = mutableListOf<BoardSimpleModel>()
    private val userDataList = mutableListOf<UserSimpleModel>()

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


        userDataList.add(UserSimpleModel("배성흥","컴퓨터공학과/백엔드"))
        userDataList.add(UserSimpleModel("배성흥2","컴퓨터공학과/백엔드33"))
        userDataList.add(UserSimpleModel("배성흥3","컴퓨터공학과/백엔드334"))
        userDataList.add(UserSimpleModel("배성흥4","컴퓨터공학과/백엔드555"))
        userDataList.add(UserSimpleModel("배성흥5","컴퓨터공학과/백엔드7777"))
        userDataList.add(UserSimpleModel("배성흥6","컴퓨터공학과/백엔드0000"))

        boardDataList2.add(BoardSimpleModel("제목 11", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList2.add(BoardSimpleModel("제목 22", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))
        boardDataList2.add(BoardSimpleModel("제목 334", "같이 팀플할 사람 구해요", "캡스톤디자인2", "2022-05-07"))

        val rvPopularBoard = view.findViewById<RecyclerView>(R.id.rv_popular_board_home_fragment)
        val rvPopularUser = view.findViewById<RecyclerView>(R.id.rv_popular_user_home_fragment)
        val rvNewBoard = view.findViewById<RecyclerView>(R.id.rv_new_board_home_fragment)

        homeMainRVAdapter = HomeFragementMainAdapter(boardDataList)
        rvPopularBoard.adapter = homeMainRVAdapter
        rvPopularBoard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeMainRVAdapter2 = HomeFragmentMainUserAdapter(userDataList)
        rvPopularUser.adapter = homeMainRVAdapter2
        rvPopularUser.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        homeMainRVAdapter3 = HomeFragmentMainNewBoardAdapter(boardDataList2)
        rvNewBoard.adapter = homeMainRVAdapter3
        rvNewBoard.layoutManager = LinearLayoutManager(context)

        return view
    }

}