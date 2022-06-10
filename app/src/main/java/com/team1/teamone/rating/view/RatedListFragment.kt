package com.team1.teamone.rating.view

import android.content.Intent
import android.media.Rating
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.presenter.AppealBoardAdapter
import com.team1.teamone.board.view.activity.CreateAppealBoardActivity
import com.team1.teamone.databinding.FragmentAppealBoardListBinding
import com.team1.teamone.databinding.FragmentRatedListBinding
import com.team1.teamone.databinding.FragmentRatingListBinding
import com.team1.teamone.rating.model.RatingApi
import com.team1.teamone.rating.model.RatingListResponse
import com.team1.teamone.rating.model.RatingResponse
import com.team1.teamone.rating.presenter.RatingAdapter
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RatedListFragment : Fragment() {


    private lateinit var binding : FragmentRatedListBinding
    private val api = RetrofitClient.create(RatingApi::class.java, RetrofitClient.getAuth())
    private val ratingDataList = mutableListOf<RatingResponse>()
    private lateinit var ratingAdapter : RatingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userId = PreferenceUtil.prefs.getString("userid",  "")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rated_list, container, false)
        getAllRated(userId.toLong())
        return binding.root
    }

    private fun getAllRated(memberId : Long) {
        api.getRatedList(memberId).enqueue(object : Callback<RatingListResponse> {
            override fun onResponse(call: Call<RatingListResponse>, response: Response<RatingListResponse>) {
                response.body()?.ratings?.let { it1 -> ratingDataList.addAll(it1) }
                ratingAdapter = RatingAdapter(ratingDataList)
                binding.rvRatedList.adapter = ratingAdapter
                binding.rvRatedList.layoutManager = LinearLayoutManager(
                    this@RatedListFragment.context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
            }
            override fun onFailure(call: Call<RatingListResponse>, t: Throwable) {
                // 실패
                Log.d("GET Board ALL", t.message.toString())
                Log.d("GET Board ALL", "fail")
            }

        })
    }
}