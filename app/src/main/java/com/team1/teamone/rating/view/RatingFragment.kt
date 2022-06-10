package com.team1.teamone.rating.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentRatingBinding
import com.team1.teamone.databinding.FragmentRatingListBinding

class RatingFragment : Fragment() {

    private lateinit var binding : FragmentRatingBinding
//    private val api = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
//    private val boardDataList = mutableListOf<BoardResponse>()
//    private lateinit var appealBoardAdapter : AppealBoardAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating, container, false)
        return binding.root
    }
}