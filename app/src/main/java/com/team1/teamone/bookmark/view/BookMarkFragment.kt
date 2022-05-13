package com.team1.teamone.bookmark.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.bookmark.model.BookMarkApi
import com.team1.teamone.bookmark.model.BookMarkListResponse
import com.team1.teamone.bookmark.model.BookMarkResponse
import com.team1.teamone.bookmark.presenter.BookMarkAdapter
import com.team1.teamone.databinding.FragmentBookmarkBinding
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookMarkFragment : Fragment() {
    private lateinit var binding : FragmentBookmarkBinding
    private val api = RetrofitClient.create(BookMarkApi::class.java, RetrofitClient.getAuth())
    private val bookMarkDataList = mutableListOf<BookMarkResponse>()
    private lateinit var bookMarkAdapter : BookMarkAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bookmark, container, false)

        api.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(call: Call<BookMarkListResponse>, response: Response<BookMarkListResponse>) {
                // 성공
                response.body()?.bookMarks?.let {it1 -> bookMarkDataList.addAll(it1)}
                bookMarkAdapter = BookMarkAdapter(bookMarkDataList)
                binding.rvBookmark.adapter = bookMarkAdapter
                binding.rvBookmark.layoutManager = LinearLayoutManager(this@BookMarkFragment.context, LinearLayoutManager.VERTICAL, false)

            }

            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.d("GET Board ALL",t.message.toString())
                Log.d("GET Board ALL","fail")
            }
        })

        return binding.root

    }
}