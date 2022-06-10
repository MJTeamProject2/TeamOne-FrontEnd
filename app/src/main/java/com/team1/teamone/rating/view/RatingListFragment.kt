package com.team1.teamone.rating.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.board.presenter.FreeBoardAdapter
import com.team1.teamone.board.view.activity.BoardDetailActivity
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

class RatingListFragment : Fragment() {

    private lateinit var binding : FragmentRatingListBinding
    private val api = RetrofitClient.create(RatingApi::class.java, RetrofitClient.getAuth())
    private val ratingDataList = mutableListOf<RatingResponse>()
    private lateinit var ratingAdapter : RatingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val userId = PreferenceUtil.prefs.getString("userid",  "")
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_rating_list, container, false)
        getAllRatings(userId.toLong())
        return binding.root
    }

    private fun getAllRatings(memberId : Long) {
        api.getRatingList(memberId).enqueue(object : Callback<RatingListResponse> {
            override fun onResponse(call: Call<RatingListResponse>, response: Response<RatingListResponse>) {
                response.body()?.ratings?.let { it1 -> ratingDataList.addAll(it1) }
                ratingAdapter = RatingAdapter(ratingDataList)
                binding.rvRatingList.adapter = ratingAdapter
                binding.rvRatingList.layoutManager = LinearLayoutManager(
                    this@RatingListFragment.context,
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