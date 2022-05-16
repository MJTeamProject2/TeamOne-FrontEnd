package com.team1.teamone.board.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse

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
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener 로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener


    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private val classTitle: TextView = itemView.findViewById(R.id.tv_classTitle)
        private val title: TextView = itemView.findViewById(R.id.tv_freeTitle)
        //private val content: TextView = itemView.findViewById(R.id.tv_content)
        //private val memberCount: TextView = itemView.findViewById(R.id.tv_memberCount)
        //private val classDate: TextView = itemView.findViewById(R.id.tv_classDate)  //직업

        fun bindItems(item: BoardResponse){
            title.text = item.title
            //content.text = item.content
           /* classTitle.text = item.classTitle
            memberCount.text = item.memberCount.toString()
            classDate.text = item.classDate*/
        }
    }

}

