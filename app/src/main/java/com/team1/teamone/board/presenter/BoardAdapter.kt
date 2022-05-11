package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.board.model.BoardSimpleModel

class BoardAdapter(val boardList: MutableList<BoardResponse>) : RecyclerView.Adapter<BoardAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {

        return boardList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(boardList[position])
    }


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val classTitle = itemView.findViewById<TextView>(R.id.tv_title)   //이름
        val title= itemView.findViewById<TextView>(R.id.tv_age)   //나이
        val content = itemView.findViewById<TextView>(R.id.tv_type)  //직업

        fun bindItems(item: BoardResponse){
            title.text = item.title
            content.text = item.content
            classTitle.text = item.classTitle
        }
    }

}
