package com.team1.teamone.home.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.home.model.UserSimpleModel

class HomeFragmentMainUserAdapter(val items : MutableList<UserSimpleModel>) :
    RecyclerView.Adapter<HomeFragmentMainUserAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homefragement_main_user_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(itemClick != null){
            holder.itemView.setOnClickListener{v->
                itemClick?.onClick(v, position)
            }
        }
        holder.bindItems(items[position])
    }

    interface ItemClick{
        fun onClick(view:View, position: Int)
    }

    var itemClick : ItemClick? = null

    override fun getItemCount(): Int {
        return items.size
    }

    inner class CustomViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView){
        private val memberName = itemView.findViewById<TextView>(R.id.rvMemberName)
        private val introduce = itemView.findViewById<TextView>(R.id.rvIntroduce)
        fun bindItems(item: UserSimpleModel){
            memberName.text = item.userName
            introduce.text = item.userInfo

        }
    }

}