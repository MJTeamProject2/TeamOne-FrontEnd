package com.team1.teamone.profile.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.team1.teamone.R
import com.team1.teamone.caution.model.CautionApi
import com.team1.teamone.caution.model.CautionResponse
import com.team1.teamone.databinding.ActivityMemberInfoBinding
import com.team1.teamone.member.view.activity.LoginActivity
import com.team1.teamone.message.model.MessageApi
import com.team1.teamone.message.model.MessageRoomRequest
import com.team1.teamone.message.model.MessageRoomResponse
import com.team1.teamone.message.view.MessageActivity
import com.team1.teamone.profile.model.ProfileApi
import com.team1.teamone.util.network.MemberResponse
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MemberInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMemberInfoBinding
    private val apiProfile = RetrofitClient.create(ProfileApi::class.java, RetrofitClient.getAuth())
    private val apiMessage = RetrofitClient.create(MessageApi::class.java, RetrofitClient.getAuth())
    private val apiCaution = RetrofitClient.create(CautionApi::class.java, RetrofitClient.getAuth())
    private var targetUserId : String? = null
    private var senderId : Long? = null
    private var receiverId : Long? = null
    private var roomId : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member_info)

        // 선택한 유저 정보 가져오기
        targetUserId = intent.getStringExtra("targetId")
        Log.d("targetId", targetUserId.toString())


        // 회원 정보 가져오기
        getMember(targetUserId.toString())

        val userid = PreferenceUtil.prefs.getString("userid",  "")
        Log.d("userid", userid)

        // 채팅하기
        val btnMessageRoom = findViewById<Button>(R.id.btn_messageRoom)
        btnMessageRoom.setOnClickListener {
            val messageRoomIntent = MessageRoomRequest(userid.toLong(), targetUserId?.toLong())

            postChat(messageRoomIntent)

            Handler().postDelayed({
                val roomIdIntent = roomId
                val senderIdIntent = senderId
                val receiverIdIntent = receiverId
                Log.d("roomId_MemberInfoActivity", roomIdIntent.toString())
                Log.d("roomId_MemberInfoActivity", senderIdIntent.toString())
                Log.d("roomId_MemberInfoActivity", receiverIdIntent.toString())

                val messageIntent = Intent(applicationContext, MessageActivity::class.java)
                messageIntent.putExtra("roomId", roomId.toString())
                messageIntent.putExtra("senderId", senderId.toString())
                messageIntent.putExtra("receiverId", receiverId.toString())
                startActivity(messageIntent)
            }, 2000)

        }
        val blockBtn = findViewById<Button>(R.id.btn_block)
        blockBtn.setOnClickListener {
            blockMember(targetUserId!!.toLong())
        }
    }

    private fun blockMember(targetUserId : Long) {
        apiCaution.postCaution(targetUserId).enqueue(object : Callback<CautionResponse> {
            override fun onResponse(
                call: Call<CautionResponse>,
                response: Response<CautionResponse>
            ) {
                Toast.makeText(applicationContext, "차단 했습니다.", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<CautionResponse>, t: Throwable) {

            }

        })
    }

    private fun getMember(targetUserId : String) {
        apiProfile.getMember(targetUserId).enqueue(object : Callback<MemberResponse> {
            override fun onResponse(
                call: Call<MemberResponse>,
                response: Response<MemberResponse>
            ) {
                Toast.makeText(applicationContext, "서버 연결.", Toast.LENGTH_SHORT).show()

                Log.d("getMember", response.body().toString())

                // 회원 정보 띄우기
                val tv_profileName = findViewById<TextView>(R.id.tv_profileName)
                val tvProfileNickname = findViewById<TextView>(R.id.tv_profileNickname)
                val tvProfileEmail = findViewById<TextView>(R.id.tv_profileEmail)
                val tvProfileDepartment = findViewById<TextView>(R.id.tv_profile_departmnet)
                val tvProfileSchoolId = findViewById<TextView>(R.id.tv_profile_schoolId)
                val tvProfilePoint = findViewById<TextView>(R.id.tvProfilePoint)

                tv_profileName.text= response.body()?.userName
                tvProfileNickname.text= response.body()?.nickname
                tvProfileEmail.text= response.body()?.email
                tvProfileDepartment.text= response.body()?.department
                tvProfileSchoolId.text= response.body()?.schoolId
                tvProfilePoint.text= response.body()?.points.toString()
            }

            override fun onFailure(call: Call<MemberResponse>, t: Throwable) {
                // 실패
                Log.d("getMember", t.message.toString())
                Log.d("getMember", "fail")
            }

        })
    }

    private fun postChat(messageRoomRequest : MessageRoomRequest) {
        apiMessage.postMessageRoom(messageRoomRequest).enqueue(object : Callback<MessageRoomResponse>{
            override fun onResponse(
                call: Call<MessageRoomResponse>,
                response: Response<MessageRoomResponse>
            ) {
                Log.d("postChat", response.body().toString())
                val RcreatedDate = response.body()?.createdDate
                val RmessageRoomId = response.body()?.messageRoomId
                val RreceiverUserId = response.body()?.receiverUserId
                val RsenderUserId = response.body()?.senderUserId
                val RsenderId = response.body()?.senderId
                val RreceiverId = response.body()?.receiverId


                Log.d("postChat createdDate", RcreatedDate.toString())
                Log.d("postChat messageRoomId", RmessageRoomId.toString())
                Log.d("postChat receiverUserId", RreceiverUserId.toString())
                Log.d("postChat senderUserId", RsenderUserId.toString())
                Log.d("postChat senderId", RsenderId.toString())
                Log.d("postChat receiverId", RreceiverId.toString())


                roomId = response.body()?.messageRoomId
                senderId = response.body()?.senderId
                receiverId = response.body()?.receiverId
            }

            override fun onFailure(call: Call<MessageRoomResponse>, t: Throwable) {
                // 실패
                Log.d("postChat", t.message.toString())
                Log.d("postChat", "fail")
            }

        })
    }
}