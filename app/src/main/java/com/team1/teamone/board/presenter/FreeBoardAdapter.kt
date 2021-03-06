package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.CommentResponse
import com.team1.teamone.util.network.MemberResponse

class FreeBoardAdapter(private val boardList: MutableList<BoardResponse>) : RecyclerView.Adapter<FreeBoardAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.free_list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return boardList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(boardList[position])
        holder.itemView.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
    }
    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }

    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }

    private lateinit var itemClickListener : OnItemClickListener


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_free_list_item_title)
        val writerNickname: TextView = itemView.findViewById(R.id.tv_free_list_item_writerNickname)
//        val createdDate: TextView = itemView.findViewById(R.id.tv_freeBoardCreatedDate)
//        val viewCount: TextView = itemView.findViewById(R.id.tv_freeBoardViewCount)


        fun bindItems(item: BoardResponse){
            title.text = item.title
            writerNickname.text = item.writer?.nickname.toString()
//            createdDate.text = item.createdDate
//            viewCount.text = item.viewCount.toString()
        }
    }

}

