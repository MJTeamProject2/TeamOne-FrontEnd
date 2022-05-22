package com.team1.teamone.message.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.message.model.MessageRoomResponse

class MessageRoomListRVAdaper(val items: MutableList<MessageRoomResponse>) :
    RecyclerView.Adapter<MessageRoomListRVAdaper.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.message_room_rv_item ,parent, false)
        return ViewHolder(view)
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nickname: TextView = itemView.findViewById(R.id.rv_nickname_message_room)
        private val contents :TextView = itemView.findViewById(R.id.rvContents)
        private val date :TextView = itemView.findViewById(R.id.rvDate_message_room)

        fun bindItems(item: MessageRoomResponse){
            nickname.text = item.messageRoomId.toString();
            contents.text = item.receiverUserId
            date.text = item.createdDate
        }
    }


}