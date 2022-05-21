package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.CommentResponse

class CommentAdapter(private val commentList: MutableList<CommentResponse>) : RecyclerView.Adapter<CommentAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.free_comment_list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(commentList[position])
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
        private val writer: TextView = itemView.findViewById(R.id.tv_commentWirter)
        private val content: TextView = itemView.findViewById(R.id.tv_commentContent)
        private val date: TextView = itemView.findViewById(R.id.tv_commentUpdateDate)


        fun bindItems(item: CommentResponse) {
            writer.text = item.writer?.nickname.toString()
            content.text = item.content
            date.text = item.updatedDate
        }
    }

}
