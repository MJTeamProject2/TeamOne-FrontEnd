package com.team1.teamone.message.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.message.model.*
import com.team1.teamone.message.presenter.MessageListRVAdapter
import com.team1.teamone.profile.model.ProfileApi
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.concurrent.thread

class MessageActivity : AppCompatActivity() {

    private lateinit var messageListRVAdaper: MessageListRVAdapter
    private val messageList = mutableListOf<MessageResponse>()
    private val apiMessage = RetrofitClient.create(MessageApi::class.java, RetrofitClient.getAuth())
    private var roomId : String? = null
    private var senderId : String? = null
    private var receiverId : String? = null

    override fun onResume() {
        super.onResume()
        // 매 10초마다 실행
        // 문제점 : 실행될 때마다 새로 그려짐
        thread(start=true) {
            while (true){
                Thread.sleep(10000)
                runOnUiThread {
                    getMessageList(roomId!!.toLong())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val userid = PreferenceUtil.prefs.getString("userid",  "")
        Log.d("userid", userid)

        roomId = intent.getStringExtra("roomId")
        senderId = intent.getStringExtra("senderId")
        receiverId = intent.getStringExtra("receiverId")

        Log.e("roomId", roomId.toString())
        Log.e("senderUserId", senderId.toString())
        Log.e("receiverUserId", receiverId.toString())

        var InReceiverId: String
        var InSenderId: String


        // 누가 전송자인지 구분
        Log.d("message Sender ", senderId.toString())
        Log.d("message userid ", userid)

        if (senderId == userid) {
            InSenderId = userid.toString()
            InReceiverId = receiverId.toString()
            
            Log.d("InSenderUserId", InSenderId)
            Log.d("InReceiverUserId", InReceiverId)
        } else {
            InReceiverId = senderId.toString()
            InSenderId = userid.toString()
            Log.d("InSenderUserId", InSenderId)
            Log.d("InReceiverUserId", InReceiverId)
        }

        Log.d("RoomId_MessageActivity", roomId.toString())

        val editMessage = findViewById<EditText>(R.id.edt_messageEdit)
        val input = findViewById<Button>(R.id.btn_messageInputBtn)
        input.setOnClickListener {
            Handler().postDelayed({
                if(editMessage.text.toString() != null){
                    Log.d("Message Check0",editMessage.text.toString())
                    val toEdit = editMessage.text.toString()
                    val messageRequest = MessageRequest(
                        InSenderId.toLong(),
                        InReceiverId.toLong(),
                        toEdit,
                        roomId!!.toLong()
                    )
                    postMessage(messageRequest)
                }
            },100)
        }

        val backBtn = findViewById<ImageView>(R.id.iv_messageBack)
        backBtn.setOnClickListener {
            finish()
        }
    }

    private fun getMessageList(messageRoomId : Long) {
        apiMessage.getMessageList(messageRoomId).enqueue(object : Callback<MessageListResponse>{
            override fun onResponse(
                call: Call<MessageListResponse>,
                response: Response<MessageListResponse>
            ) {
                Log.d("GET MESSAGE ALL", response.body().toString())

                // 기존에 있던거 삭제
                messageList.clear()

                response.body()?.messageList?.let { it -> messageList.addAll(it) }
                // 리사이클러뷰
                val messageRV = findViewById<RecyclerView>(R.id.rv_RecyclerViewMessage)
                messageListRVAdaper = MessageListRVAdapter(messageList)
                messageRV.adapter = messageListRVAdaper
                messageRV.layoutManager =
                    LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }

            override fun onFailure(call: Call<MessageListResponse>, t: Throwable) {
                // 실패
                Log.d("GET MESSAGE ALL", t.message.toString())
                Log.d("GET MESSAGE ALL", "fail")
            }
        })
    }

    private fun postMessage(messageRequest: MessageRequest) {
        apiMessage.postMessage(messageRequest).enqueue(object : Callback<MessageResponse> {
            override fun onResponse(
                call: Call<MessageResponse>,
                response: Response<MessageResponse>
            ) {
                Log.d("POST MESSAGE", response.body().toString())

                // 보낸 후 메시지 가져오기
                getMessageList(roomId!!.toLong())

                // editText 내용 삭제
                val editMessage = findViewById<EditText>(R.id.edt_messageEdit)
                editMessage.setText("")

                //메시지를 보낼 시 화면을 맨 밑으로 내림
                val messageRv = findViewById<RecyclerView>(R.id.rv_RecyclerViewMessage)
                messageRv?.scrollToPosition(messageList.size - 1)
            }

            override fun onFailure(call: Call<MessageResponse>, t: Throwable) {
                // 실패
                Log.d("POST MESSAGE", t.message.toString())
                Log.d("POST MESSAGE", "fail")
            }

        })
    }
}