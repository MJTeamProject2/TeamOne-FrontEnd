package com.team1.teamone.message.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentMessageBinding
import com.team1.teamone.message.model.MessageApi
import com.team1.teamone.message.model.MessageRoomListResponse
import com.team1.teamone.message.model.MessageRoomResponse
import com.team1.teamone.message.presenter.MessageRoomListRVAdaper
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MessageFragment : Fragment() {

    private lateinit var messageRoomListRVAdaper: MessageRoomListRVAdaper
    private val messageRoomList = mutableListOf<MessageRoomResponse>()
    private val api = RetrofitClient.create(MessageApi::class.java, RetrofitClient.getAuth())
    private lateinit var binding : FragmentMessageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_message, container, false)
        // 유저 정보 가져오기기
        val userid= PreferenceUtil.prefs.getString("userid",  "")
        Log.d("userid userid", userid)

        drawMessageRoomList(userid.toLong())

        // 새로 고침 시 로직
        binding.swipeMessage.setOnRefreshListener {
            drawMessageRoomList(userid.toLong())
        }

        return binding.root
    }

    private fun drawMessageRoomList(receiverId : Long) {
        api.getMessageRoomList(receiverId).enqueue(object : Callback<MessageRoomListResponse> {
            override fun onResponse(
                call: Call<MessageRoomListResponse>,
                response: Response<MessageRoomListResponse>
            ) {
                Log.d("GET MESSAGE ALL", response.body().toString())

                messageRoomList.clear()

                response.body()?.messageRoomList?.let { it -> messageRoomList.addAll(it) }

                messageRoomListRVAdaper = MessageRoomListRVAdaper(messageRoomList)
                binding.rvMessageRoomList.adapter = messageRoomListRVAdaper

                // 리사이클러뷰 보기 형식
                binding.rvMessageRoomList.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                // 새로 고침 멈춤
                binding.swipeMessage.isRefreshing = false

                messageRoomListRVAdaper.itemClick = object : MessageRoomListRVAdaper.ItemClick {
                    override fun onClick(view: View, position: Int) {
                        val intent = Intent(binding.rvMessageRoomList.context, MessageActivity::class.java)
                        intent.putExtra("roomId", messageRoomList[position].messageRoomId.toString())
                        intent.putExtra("senderId", messageRoomList[position].senderId.toString())
                        intent.putExtra("receiverId", messageRoomList[position].receiverId.toString())
                        Log.d("roomId_MessageFragment",
                            messageRoomList[position].messageRoomId.toString()
                        )
                        context?.startActivity(intent)
                    }

                }
            }

            override fun onFailure(call: Call<MessageRoomListResponse>, t: Throwable) {
                // 실패
                Log.d("GET MESSAGE ROOM ALL", t.message.toString())
                Log.d("GET MESSAGE ROOM ALL", "fail")
            }
        })
    }
}