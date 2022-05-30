package com.team1.teamone.bookmark.presenter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.team1.teamone.R
import com.team1.teamone.bookmark.model.BookMarkResponse

class BookMarkAdapter(private val bookMarkList : MutableList<BookMarkResponse>) : RecyclerView.Adapter<BookMarkAdapter.BookMarkViewHolder>(){
    /*
    viewHolder 의 역할 : 리사이클러뷰가 아이템을 계속 순환하면서 보여줄려면
    아이템에 대한 정보를 기억하고 있을 녀석이 필요한데, 그게 홀더의 역할임
    어댑터 : 아이템과 리사이클러뷰를 바인딩 해주는 녀석
    LayoutManager : 스크롤을 위로할지 아래로할지 등등 옵션을 정하는 녀석 (더 많은 기능이 있음)
     */

    /*
    getItemCount 호출 된 후 호출되는 메소드
    이름과 같이, ViewHolder 를 생성한다
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookMarkViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.bookmark_item_list, parent, false)
        return BookMarkViewHolder(view)
    }


    /*
    가장 먼저 호출되는 메소드
    리사이클러뷰에서 보여줄 아이템의 갯수를 정해준다
     */
    override fun getItemCount(): Int {
        return bookMarkList.size
    }

    /*
    생성한 ViewHolder 에 데이터를 바인딩하는 메소드
     */
    override fun onBindViewHolder(holder: BookMarkViewHolder, position: Int) {

        // 북마크 버튼 다시 누르기 -> 북마크 해제
        holder.btnBookMark.setOnClickListener {
            itemClickListener.onClick(it, position)
        }
        holder.tvTitle.text = "게시글 제목 : "+bookMarkList[position].board.title
//        holder.tvBookMarkId.text = "ID : " + bookMarkList[position].bookMarkId.toString()
        holder.tvType.text = "게시물 종류 : "+when(bookMarkList[position].board.boardType) {
            "FREE" -> "자유"
            "APPEAL" -> "어필해요"
            "WANTED" -> "팀원구해요"
            else -> "오류"
        }
        holder.tvTime.text = bookMarkList[position].createdDate

    }

    interface OnItemClickListener {
        fun onClick(v: View, position: Int)
    }
    // (3) 외부에서 클릭 시 이벤트 설정
    fun setItemClickListener(onItemClickListener: OnItemClickListener) {
        this.itemClickListener = onItemClickListener
    }
    // (4) setItemClickListener로 설정한 함수 실행
    private lateinit var itemClickListener : OnItemClickListener

    // ViewHolder 를 정의
    class BookMarkViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
//        val tvBookMarkId: TextView = itemView.findViewById(R.id.)
        val btnBookMark: ImageView = itemView.findViewById(R.id.btn_bookMarkImg)
        val tvTitle: TextView = itemView.findViewById(R.id.tv_bookmark_title)
        val tvType:TextView = itemView.findViewById(R.id.tv_bookmark_boardType)
        val tvTime: TextView = itemView.findViewById(R.id.tv_bookmark_createTime)
    }
}