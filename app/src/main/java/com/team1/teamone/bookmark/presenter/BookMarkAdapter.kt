package com.team1.teamone.bookmark.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse

class BookMarkAdapter(val bookMarkList : ArrayList<BoardResponse>) : RecyclerView.Adapter<BookMarkAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_summary, parent, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return bookMarkList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvTitle.text = bookMarkList.get(position).title

    }

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val btnBookMark = itemView.findViewById<ImageView>(R.id.btn_bookmark)
        val tvTitle = itemView.findViewById<TextView>(R.id.tv_title)
        val tvType = itemView.findViewById<TextView>(R.id.tv_type)

    }
}