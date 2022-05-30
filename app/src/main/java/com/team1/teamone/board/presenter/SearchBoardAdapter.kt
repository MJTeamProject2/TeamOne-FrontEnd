package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse

class SearchBoardAdapter(private val boardList: MutableList<BoardResponse>) : RecyclerView.Adapter<SearchBoardAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_board_rv_item, parent, false)
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
        private val boardType: TextView = itemView.findViewById(R.id.tv_searchBoardType)
        private val writerNickname: TextView = itemView.findViewById(R.id.tv_searchBoardWriter)
        private val title: TextView = itemView.findViewById(R.id.tv_searchBoardTitle)

        fun bindItems(item: BoardResponse){
            boardType.text = when(item.boardType) {
                "WANTED" -> "팀원구해요"
                "APPEAL" -> "어필해요"
                else -> "자유"
            }
            writerNickname.text = "작성자 : " + item.writer?.nickname
            title.text = "제목 : " + item.title
        }
    }

}
