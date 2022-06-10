package com.team1.teamone.rating.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.MemberBoardResponse

class CreateRatingRVAdapter(private val ratingList: MutableList<MemberBoardResponse>) :
    RecyclerView.Adapter<CreateRatingRVAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CreateRatingRVAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rating_check_list, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CreateRatingRVAdapter.CustomViewHolder, position: Int) {
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
        val title: TextView = itemView.findViewById(R.id.tv_item_title)
//        val writerNickname: TextView = itemView.findViewById(R.id.tv_item_writerNickname)
//        val viewCount: TextView = itemView.findViewById(R.id.tv_freeBoardViewCount)


        fun bindItems(item: MemberBoardResponse){
            title.text = item.memberBoardId.toString()
//            title.text = item.title
//            writerNickname.text = item.writer?.nickname.toString()
//            createdDate.text = item.createdDate
//            viewCount.text = item.viewCount.toString()
        }
    }


}