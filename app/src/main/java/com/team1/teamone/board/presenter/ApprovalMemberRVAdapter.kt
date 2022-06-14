package com.team1.teamone.board.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.board.model.BoardApi
import com.team1.teamone.board.model.MemberBoardResponse
import com.team1.teamone.util.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApprovalMemberRVAdapter(private val items: MutableList<MemberBoardResponse>) :
    RecyclerView.Adapter<ApprovalMemberRVAdapter.CustomViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ApprovalMemberRVAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.approval_rv_item, parent, false)
        return CustomViewHolder(view)
    }

    interface ItemClick{
        fun onClick(view: View, position: Int)
    }
    var itemClick : ItemClick? = null


    override fun onBindViewHolder(holder: ApprovalMemberRVAdapter.CustomViewHolder, position: Int) {
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

    private val boardApi = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())

    inner class CustomViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val nickname : TextView = itemView.findViewById(R.id.rv_nickname_approval)
        val image : ImageView = itemView.findViewById(R.id.imageView_approval)
        val approvalBtn : ImageView = itemView.findViewById(R.id.btn_approval)
        val noApprovalBtn : ImageView = itemView.findViewById(R.id.btn_no_approval)

        fun bindItems(item: MemberBoardResponse){
            nickname.text = item.nickname

            val memberBoardId = item.memberBoardId
            Log.d("memberBoardId", memberBoardId.toString())

            approvalBtn.setOnClickListener {
                Log.d("click ApprovalMemberRVAdapter", "OK")
                if (memberBoardId != null) {
                    boardApi.postApprovalMemberBoard(memberBoardId).enqueue(object :
                        Callback<MemberBoardResponse>{
                        override fun onResponse(
                            call: Call<MemberBoardResponse>,
                            response: Response<MemberBoardResponse>
                        ) {
                            Toast.makeText(itemView.context, "승인 완료!", Toast.LENGTH_SHORT).show()

                            Log.d("ApprovalMemberRVAdapter postApprovalMemberBoard", response.body().toString())
                        }

                        override fun onFailure(call: Call<MemberBoardResponse>, t: Throwable) {
                            // 실패
                            Log.e("postApprovalMemberBoard", "실패")
                        }

                    })
                }
            }
            noApprovalBtn.setOnClickListener {
                Log.d("click NoApprovalMemberRVAdapter", "No")
                if (memberBoardId != null) {
                    boardApi.postNoApprovalMemberBoard(memberBoardId).enqueue(object :
                        Callback<MemberBoardResponse> {
                        override fun onResponse(
                            call: Call<MemberBoardResponse>,
                            response: Response<MemberBoardResponse>
                        ) {
                            Toast.makeText(itemView.context, "승인 거부", Toast.LENGTH_SHORT).show()
                            Log.d("ApprovalMemberRVAdapter postNoApprovalMemberBoard", response.body().toString())
                        }

                        override fun onFailure(call: Call<MemberBoardResponse>, t: Throwable) {
                            // 실패
                            Log.e("postNoApprovalMemberBoard", "실패")
                        }

                    })
                }

            }


        }
    }
}