package com.team1.teamone.home.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.home.model.BoardSimpleModel

class HomeFragmentMainNewBoardAdapter (val items : MutableList<BoardSimpleModel>) : RecyclerView.Adapter<HomeFragmentMainNewBoardAdapter.CustomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.homefragment_main_rv_item_new, parent, false)
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
        private val title = itemView.findViewById<TextView>(R.id.rvTitle)
        private val content = itemView.findViewById<TextView>(R.id.rvContents)
        private val classname = itemView.findViewById<TextView>(R.id.rvClassName)
        private val createDate = itemView.findViewById<TextView>(R.id.rvDate)
        fun bindItems(item: BoardSimpleModel){
            title.text = item.title
            content.text = item.content
            classname.text = item.classname
            createDate.text = item.createdDate
        }
    }
}
