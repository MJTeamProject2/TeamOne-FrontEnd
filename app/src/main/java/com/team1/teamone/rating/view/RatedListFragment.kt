package com.team1.teamone.rating.view

import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.presenter.AppealBoardAdapter
import com.team1.teamone.board.view.activity.CreateAppealBoardActivity
import com.team1.teamone.databinding.FragmentAppealBoardListBinding
import com.team1.teamone.databinding.FragmentRatingListBinding
import com.team1.teamone.rating.model.RatingApi
import com.team1.teamone.rating.model.RatingResponse
import com.team1.teamone.util.network.RetrofitClient

class RatedListFragment : Fragment() {

    private lateinit var binding : FragmentRatingListBinding
    private val api = RetrofitClient.create(RatingApi::class.java, RetrofitClient.getAuth())
    private val ratingDataList = mutableListOf<RatingResponse>()
    private lateinit var appealBoardAdapter : AppealBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating_list, container, false)
        return binding.root
    }
}