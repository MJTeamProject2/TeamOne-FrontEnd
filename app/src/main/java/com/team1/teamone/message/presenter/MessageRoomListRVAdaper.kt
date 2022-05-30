package com.team1.teamone.message.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.message.model.MessageRoomResponse
import com.team1.teamone.util.view.PreferenceUtil

class MessageRoomListRVAdaper(val items: MutableList<MessageRoomResponse>) :
    RecyclerView.Adapter<MessageRoomListRVAdaper.ViewHolder>() {
    val userid = PreferenceUtil.prefs.getString("userid",  "")

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return when (viewType) {
            0-> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_room_rv_item, parent, false)
                ViewHolder(view)
            }
            else ->{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.message_room_rv_item, parent, false)
                ViewHolder(view)
            }
        }
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

    override fun getItemViewType(position: Int): Int {
        Log.d("userid", userid)
        return if(items[position].senderId == userid.toLong()){
            0
        }else{
            1
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nickname: TextView = itemView.findViewById(R.id.rv_nickname_message_room1)
        private val contents :TextView = itemView.findViewById(R.id.rvContents)
        private val date :TextView = itemView.findViewById(R.id.rvDate_message_room)

        fun bindItems(item: MessageRoomResponse){
//            nickname.text = item.messageRoomId.toString();
//            contents.text = item.receiverUserId
            if (userid.toLong() == item.receiverId) {
                nickname.text = item.senderUserId
            } else {
                nickname.text = item.receiverNickname
            }
            date.text = item.createdDate
        }
    }


}