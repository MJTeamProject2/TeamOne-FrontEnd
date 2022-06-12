package com.team1.teamone.board.presenter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.MemberBoardResponse
import com.team1.teamone.profile.view.MemberInfoActivity
import com.team1.teamone.util.network.MemberResponse

class ParticipatingMemberRVAdapter(private val items: MutableList<MemberBoardResponse>) :
    RecyclerView.Adapter<ParticipatingMemberRVAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ParticipatingMemberRVAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.participating_member_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }
    var itemClick : ItemClick? = null

    override fun onBindViewHolder(holder: ParticipatingMemberRVAdapter.CustomViewHolder, position: Int) {
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

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nickname : TextView = itemView.findViewById(R.id.rv_nickname_participate)
        val image : ImageView = itemView.findViewById(R.id.imageView_participate)

        fun bindItems(item: MemberBoardResponse){
            nickname.text = item.nickname
            var id = item.member?.memberId

            nickname.setOnClickListener {
                val intent = Intent(itemView.context, MemberInfoActivity::class.java)
                intent.putExtra("targetId", id.toString())
                ContextCompat.startActivity(itemView.context, intent, null)
            }
        }
    }

}