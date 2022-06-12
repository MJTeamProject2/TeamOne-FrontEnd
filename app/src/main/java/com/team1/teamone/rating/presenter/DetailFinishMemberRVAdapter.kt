package com.team1.teamone.rating.presenter

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.RatingBar.OnRatingBarChangeListener
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.MemberBoardResponse
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.profile.view.MemberInfoActivity
import com.team1.teamone.rating.model.RatingApi
import com.team1.teamone.rating.model.RatingRequest
import com.team1.teamone.rating.model.RatingResponse
import com.team1.teamone.util.network.MemberResponse
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailFinishMemberRVAdapter(private val ratingList: MutableList<MemberBoardResponse>) :
    RecyclerView.Adapter<DetailFinishMemberRVAdapter.CustomViewHolder>() {
    private val api = RetrofitClient.create(RatingApi::class.java, RetrofitClient.getAuth())

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): DetailFinishMemberRVAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_member_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: DetailFinishMemberRVAdapter.CustomViewHolder,
        position: Int
    ) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(ratingList[position])
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ratingNickname: TextView = itemView.findViewById(R.id.tv_rating_nickname)
        private val ratingBarPoint: RatingBar = itemView.findViewById(R.id.ratingBar)
        private val radioGroup : RadioGroup = itemView.findViewById(R.id.radioGroup)
        private val submitBtn :Button = itemView.findViewById(R.id.ratingBtnGo)


        var ratingPoint = 1.0
        var saveBadge = 0

        fun bindItems(item: MemberBoardResponse){
            ratingBarPoint.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
                ratingPoint = ratingBarPoint.rating.toDouble()
            }
            ratingNickname.text = item.board?.writer?.nickname
            radioGroup.setOnCheckedChangeListener{ _, checkedId ->
                Log.d("RadioButton", "RadioButton is Clicked")
                when (checkedId) {
                    R.id.radio1 -> {
                        saveBadge = 0
                        Log.d(" ", "Apple is selected")
                    }
                    R.id.radio2 -> {
                        saveBadge = 1
                        Log.d(" ", "Banana is selected")
                    }
                    R.id.radio3 -> {
                        saveBadge = 2
                        Log.d(" ", "Orange is selected")
                    }
                    R.id.radio4 -> {
                        saveBadge = 3
                        Log.d(" ", "Orange is selected")
                    }
                    R.id.radio5 -> {
                        saveBadge = 4
                        Log.d(" ", "Orange is selected")
                    }
                }
            }

            submitBtn.setOnClickListener {
                Log.d("test1", saveBadge.toString())
                Log.d("test222", ratingPoint.toString())

                val memberBoardId = item.memberBoardId!!.toLong()
                val ratingMemberId =  PreferenceUtil.prefs.getString("userid",  "").toLong()
                val ratedMemberId = item.member?.memberId!!.toLong()

                val ratingRequest = RatingRequest(memberBoardId, ratingMemberId, ratedMemberId,ratingPoint, saveBadge)
                api.postRating(ratingRequest).enqueue(object : Callback<RatingResponse> {
                    override fun onResponse(
                        call: Call<RatingResponse>,
                        response: Response<RatingResponse>
                    ) {
                        Log.e("postRating", " postRating Success")
                        Toast.makeText(itemView.context, "평가 완료!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(itemView.context, HomeActivity::class.java)
                        ContextCompat.startActivity(itemView.context, intent, null)
                    }

                    override fun onFailure(call: Call<RatingResponse>, t: Throwable) {
                        Log.e("postRating", " postRating Fail")
                    }

                })
            }

        }
    }


}