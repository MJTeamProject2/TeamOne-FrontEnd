package com.team1.teamone.rating.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.CommentResponse
import com.team1.teamone.rating.model.RatingResponse
import com.team1.teamone.util.network.MemberResponse

class RatingAdapter(private val ratingList: MutableList<RatingResponse>) : RecyclerView.Adapter<RatingAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_item_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(ratingList[position])
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ratedMemberNickname: TextView = itemView.findViewById(R.id.tv_rating_item_list_ratedMemberNickname)
        private val ratedStar: TextView = itemView.findViewById(R.id.tv_rating_item_list_star)
        private val badge: TextView = itemView.findViewById(R.id.tv_rating_item_list_badge)

        fun bindItems(item: RatingResponse){
            ratedMemberNickname.text = item.ratedMember.nickname
            ratedStar.text = item.star.toString()
            badge.text = when(item.badge) {
                1 -> "답장이 빨라요"
                2 -> "시간약속을 잘 지켜요"
                3 -> "발표를 잘해요"
                4 -> "리더십 있어요"
                else -> "하드캐리"
            }
        }
    }

}

