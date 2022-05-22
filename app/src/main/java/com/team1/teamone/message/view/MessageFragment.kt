package com.team1.teamone.message.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.databinding.FragmentMessageBinding
import com.team1.teamone.message.presenter.MessageRoomListRVAdaper
import com.team1.teamone.message.model.MessageApi
import com.team1.teamone.message.model.MessageRoomListResponse
import com.team1.teamone.message.model.MessageRoomResponse
import com.team1.teamone.profile.view.MemberInfoActivity
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
        binding.testBtn.setOnClickListener {

            Log.d("testBtn testBtn", "testBtn")

            val intent = Intent(context, MemberInfoActivity::class.java)
            intent.putExtra("targetId", "1")
            startActivity(intent)
        }

        // 유저 정보 가져오기기
        val userid= PreferenceUtil.prefs.getString("userid",  "")
        Log.d("userid userid", userid)

        drawMessageRoomList(userid.toLong())

        return binding.root
    }

    private fun drawMessageRoomList(receiverId : Long) {
        api.getMessageRoomList(receiverId).enqueue(object : Callback<MessageRoomListResponse> {
            override fun onResponse(
                call: Call<MessageRoomListResponse>,
                response: Response<MessageRoomListResponse>
            ) {
                Log.d("GET MESSAGE ALL", response.body().toString())
                response.body()?.messageRoomList?.let { it -> messageRoomList.addAll(it) }

                messageRoomListRVAdaper = MessageRoomListRVAdaper(messageRoomList)
                binding.rvMessageRoomList.adapter = messageRoomListRVAdaper

                // 리사이클러뷰 보기 형식
                binding.rvMessageRoomList.layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
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