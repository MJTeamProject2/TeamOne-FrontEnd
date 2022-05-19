package com.team1.teamone.caution.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.caution.model.CautionResponse

class CautionAdapter(private val cautionList: MutableList<CautionResponse>) : RecyclerView.Adapter<CautionAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.caution_list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cautionList.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bindItems(cautionList[position])
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
        private val cautionedUserId: TextView = itemView.findViewById(R.id.tv_cautionedUserId)
        private val cautionedUserNickname: TextView = itemView.findViewById(R.id.tv_cautionedUserNickname)
        private val cautionedDate: TextView = itemView.findViewById(R.id.tv_cautionedDate)
        private val btnCaution: ImageView = itemView.findViewById(R.id.btn_caution)

        fun bindItems(item: CautionResponse){
            cautionedUserId.text = item.cautionedMember.userId.toString()
            cautionedUserNickname.text = item.cautionedMember.nickname
            cautionedDate.text = item.createdDate
            btnCaution.setOnClickListener {
                itemClickListener.onClick(it, position)
            }
        }
    }

}

