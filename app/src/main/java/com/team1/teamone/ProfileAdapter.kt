package com.team1.teamone

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ProfileAdapter(val profileList: ArrayList<Profiles>) : RecyclerView.Adapter<ProfileAdapter.CustomViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return CustomViewHolder(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileAdapter.CustomViewHolder, position: Int) {
        holder.gender.setImageResource(profileList.get(position).gender) //현재 클릭한 위치
        holder.name.text = profileList.get(position).name
        holder.age.text = profileList.get(position).age.toString()
        holder.job.text = profileList.get(position).job
    }


    class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val gender = itemView.findViewById<ImageView>(R.id.imageView3)  //성별
        val name = itemView.findViewById<TextView>(R.id.tv_name2)   //이름
        val age = itemView.findViewById<TextView>(R.id.tv_age)   //나이
        val job = itemView.findViewById<TextView>(R.id.tv_job)  //직업
    }

}