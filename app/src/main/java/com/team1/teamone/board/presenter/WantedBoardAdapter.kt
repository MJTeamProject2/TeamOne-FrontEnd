package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse

class WantedBoardAdapter(private val boardList: MutableList<BoardResponse>) : RecyclerView.Adapter<WantedBoardAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.wanted_list_item, parent, false)
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
        private val classTitle: TextView = itemView.findViewById(R.id.tv_wanted_list_item_classTitle)
        private val title: TextView = itemView.findViewById(R.id.tv_wanted_list_item_title)
        private val memberCount: TextView = itemView.findViewById(R.id.tv_wanted_list_item_memberCount)
        private val classDate: TextView = itemView.findViewById(R.id.tv_wanted_list_item_classDate)

        fun bindItems(item: BoardResponse){
            title.text = item.title
            classTitle.text = item.classTitle
            memberCount.text = item.memberCount.toString()
            classDate.text = item.classDate
        }
    }

}

