package com.team1.teamone.rating.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.MemberBoardResponse

class DetailFinishMemberRVAdapter(private val ratingList: MutableList<MemberBoardResponse>) :
    RecyclerView.Adapter<DetailFinishMemberRVAdapter.CustomViewHolder>() {
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
        holder.bindItems(ratingList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }

    override fun getItemCount(): Int {
        return ratingList.size
    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.tv_item_title)
        private val writerNickname: TextView = itemView.findViewById(R.id.tv_item_writerNickname)


        fun bindItems(item: MemberBoardResponse){
            title.text = item.board?.title
            writerNickname.text = item.board?.writer?.nickname

        }
    }
}