package com.team1.teamone.board.view.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.team1.teamone.R
import com.team1.teamone.board.model.*
import com.team1.teamone.board.presenter.ApprovalMemberRVAdapter
import com.team1.teamone.board.presenter.CommentAdapter
import com.team1.teamone.board.presenter.ParticipatingMemberRVAdapter
import com.team1.teamone.bookmark.model.BookMarkApi
import com.team1.teamone.bookmark.model.BookMarkListResponse
import com.team1.teamone.bookmark.model.BookMarkResponse
import com.team1.teamone.caution.presenter.CautionAdapter
import com.team1.teamone.databinding.ActivityBoardDetailBinding
import com.team1.teamone.home.view.HomeActivity
import com.team1.teamone.message.presenter.MessageRoomListRVAdaper
import com.team1.teamone.util.network.BoolResponse
import com.team1.teamone.util.network.MemberResponse
import com.team1.teamone.util.network.RetrofitClient
import com.team1.teamone.util.view.PreferenceUtil
import kotlinx.android.synthetic.main.activity_board_detail.*
import kotlinx.android.synthetic.main.free_comment_list_item.*
import kotlinx.android.synthetic.main.free_comment_list_item.view.*
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BoardDetailActivity : AppCompatActivity() {

    private val boardApi = RetrofitClient.create(BoardApi::class.java, RetrofitClient.getAuth())
    private val bookMarkApi = RetrofitClient.create(BookMarkApi::class.java, RetrofitClient.getAuth())
    private val commentDataList = mutableListOf<CommentResponse>()
    private val participatingMemberList = mutableListOf<MemberBoardResponse>()
    private val approvalMemberList = mutableListOf<MemberBoardResponse>()
    private lateinit var approvalMemberRVAdapter: ApprovalMemberRVAdapter
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var binding: ActivityBoardDetailBinding
    private lateinit var participateAdapter : ParticipatingMemberRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_detail)

        val userid = PreferenceUtil.prefs.getString("userid",  "")
        Log.d("userid", userid)

        val boardType = intent.getStringExtra("detailBoardType")
        val title = intent.getStringExtra("detailTitle")
        val content = intent.getStringExtra("detailContent")
        val viewCount = intent.getIntExtra("detailViewCount", 1)
        val writer = intent.getStringExtra("detailWriter")
        val updateDate = intent.getStringExtra("detailUpdateDate")
        val boardId = intent.getLongExtra("detailBoardId",0)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)

        binding.detailTitle.text = title
        binding.tvDetailContent.text = content
        binding.tvDetailViewCount.text = "조회수:" + viewCount.toString()
        binding.tvUpdateDate.text = updateDate
        binding.tvDetailWriter.text = writer
        Log.e("boardType",boardType.toString())
        // 게시글 수정
        if(boardType.toString() == "FREE") {
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
                intent.putExtra("updateBoardId", boardId)
                startActivity(intent)
            }
        }

        if(boardType.toString() == "APPEAL") {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            binding.tvDetailClassTitle.text = classTitle
            binding.tvDetailClassDate.text = classDate

            // 게시글 수정
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateAppealBoardActivity::class.java)
                intent.putExtra("updateBoardId",boardId)
                startActivity(intent)
            }
        }

        if(boardType.toString() == "WANTED") {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            val memberCount = intent.getStringExtra("detailMemberCount")
            val deadLine = intent.getStringExtra("detailDeadline")
            binding.tvDetailMemberCount.text = memberCount
            binding.tvDetailDeadline.text = deadLine
            binding.tvDetailClassDate.text = classDate
            binding.tvDetailClassTitle.text = classTitle
            // 게시글 수정
            binding.btnBoardDetailUpdateBoard.setOnClickListener {
                val intent = Intent(applicationContext, UpdateWantedBoardActivity::class.java)
                intent.putExtra("updateBoardId",boardId)
                startActivity(intent)
            }
        }

        drawCommentList(boardId)
        updateBookMarkStar(boardId)

        // 게시글 삭제 버튼
        binding.btnBoardDetailDeleteBoard.setOnClickListener{
            deleteBoard(boardId)
        }

        //코멘트 입력 버튼
        binding.btnCommentInputBtn.setOnClickListener {
            val commentContent = edt_commentEdit.text.toString()
            val request = CommentRequest(commentContent)
            createComment(request,boardId)
        }

        //코멘트 수정 버튼

        // 북마크 (즐겨찾기 등록) 버튼
        binding.btnBoardDetailBookMark.setOnClickListener {
            toggleBookMark(boardId)
        }


        Log.d("waitMemberList", boardId.toString())

        participateMemberList(boardId)
        waitMemberList(boardId)

        // 참가하기
        binding.btnJoinBoard.setOnClickListener {

            joinMemberBoard(MemberBoardApprovalRequest(userid.toLong() , boardId))
        }
    }


    /**
     *  북마크 관련 로직------------------------------------------------------------------------------------
     */
    // 처음 게시글 상세 페이지를 켤때, 북마크로 된 게시글인지 아닌지를 검사해서 보여주기
    private fun updateBookMarkStar(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                Log.e("업데이트 북마크 호출", "성공")
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // 가져온 북마크 리스트에 해당 게시물이 있는 지 확인
                for (bookMark in bookMarkList) {
                    // 있으면 꽉찬 스타
                    if (bookMark.board.boardId == boardId) {
                        binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                        return
                    }
                }
                // 없으면 비어있는 스타
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 리스트 조회 실패", "실패")
            }

        })
    }

    // 북마크 등록
    private fun postBookMark(boardId: Long) {
        bookMarkApi.createBookMark(boardId).enqueue(object : Callback<BookMarkResponse> {
            override fun onResponse(
                call: Call<BookMarkResponse>,
                response: Response<BookMarkResponse>
            ) {
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기에 등록했습니다", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<BookMarkResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 생성 실패", "실패")
            }
        })
    }

    // 북마크 삭제
    private fun deleteBookMark(bookMarkId: Long) {
        bookMarkApi.deleteBookMark(bookMarkId).enqueue(object : Callback<BoolResponse>{
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                Toast.makeText(this@BoardDetailActivity, "즐겨찾기 목록에서 삭제했습니다", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 삭제 실패", "실패")
            }
        })
    }

    // 북마크 토글
    private fun toggleBookMark(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // 가져온 북마크 리스트에 해당 게시물이 있는 지 확인
                for (bookMark in bookMarkList) {
                    // 있으면 북마크 해제
                    if (bookMark.board.boardId == boardId) {
                        deleteBookMark(bookMark.bookMarkId)
                        return
                    }
                }
                // 없으면 북마크 등록
                postBookMark(boardId)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // 실패
                Log.e("게시판 상세 북마크 리스트 조회 실패", "실패")
            }

        })
    }
    /**
     *  코멘트 관련 로직 ------------------------------------------------------------------------------------
     */
    // 코멘트 그리기

    private fun createComment(request: CommentRequest,boardId: Long) {
        boardApi.postComment(request,boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body() == null) {
                    Log.d("comment1번", "blank")
                    return
                } else {
                    drawCommentList(boardId)
                    Log.d("comment1번", "blank")
                    Log.d("log", "success")
                }
            }
            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                // 실패
                Log.d("log", "fail")
            }
        })
    }

    private fun drawCommentList(boardId: Long) {
        boardApi.getAllComments(boardId).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                // 받아온 리스트 boardDataList 안에 넣기
                commentDataList.clear()
                response.body()?.comments?.let { it -> commentDataList.addAll(it) }

                // 리사이클러뷰 - 어뎁터 연결
                commentAdapter = CommentAdapter(commentDataList)
                binding.rvCommentList.adapter = commentAdapter

                // 리사이클러뷰 보기 형식
                binding.rvCommentList.layoutManager = LinearLayoutManager(
                    this@BoardDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                commentAdapter.setItemClickListener(object : CommentAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        deleteComment(position, boardId)
                    }
                })
            }


            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                // 실패
                Log.d("코멘트 통신 실패", "코멘트 실패")
            }
        })
    }



    private fun deleteComment(position: Int, boardId: Long) {
        AlertDialog.Builder(this)
            .setTitle("댓글 삭제")
            .setMessage("해당 댓글을 삭제하시겠습니까?")
            .setPositiveButton("예") { dialog, id ->
                deleteRequestToServer(position, boardId)
            }.setNegativeButton("아니오") { dialog, id ->

            }
            .show()
        }

    private fun deleteRequestToServer(position: Int, boardId: Long) {
        commentDataList[position].commentId?.let {
            boardApi.deleteCommentById(it).enqueue(object : Callback<BoolResponse> {
                override fun onResponse(
                    call: Call<BoolResponse>,
                    response: Response<BoolResponse>
                ) {
                    drawCommentList(boardId)
                }
                override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                    // 실패
                    Log.d("댓글 통신 실패", "댓글 하나 삭제 실패")
                }
            })
        }
    }


    /**
     * 게시글 삭제 로직------------------------------------------------------------------------------------
     */
    // 게시글 삭제 확인 다이얼로그
    private fun deleteBoard(boardId: Long) {
        AlertDialog.Builder(this)
            .setTitle("게시글 삭제")
            .setMessage("현재 게시물을 삭제 하시겠습니까?")
            .setPositiveButton("예") { dialog, id ->
                deleteRequestToServer(boardId) // 서버로 북마크 삭제 요청
            }
            .setNegativeButton("아니오") { dialog, id ->

            }
            .show()
    }

    // 서버로 게시글 삭제 요청
    private fun deleteRequestToServer(boardId: Long) {
        boardApi.deleteBoardById(boardId).enqueue(object : Callback<BoolResponse> {
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                Toast.makeText(this@BoardDetailActivity, "게시글을 삭제 했습니다", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // 실패
                Log.d("게시글 삭제 통신 실패", "게시글 삭제 실패")
            }
        })
    }


    /**
     * 참여자 목록 조회 로직------------------------------------------------------------------------------------
     */
    private fun participateMemberList(boardId : Long) {
        boardApi.getMemberBoardApproval(boardId).enqueue(object :
            Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {

                Log.d("GET participateMemberList ALL", response.body().toString())

                // 받아온 리스트 participatingMemberList 안에 넣기
                Log.d("participateMemberList", participatingMemberList.toString())
                participatingMemberList.clear()
                response.body()?.memberBoardResponseList?.let { it -> participatingMemberList.addAll(it) }

                // 참여자 목록
                participateAdapter = ParticipatingMemberRVAdapter(participatingMemberList)
                binding.rvParticipatingMember.adapter = participateAdapter
                binding.rvParticipatingMember.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            }
            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                // 실패
                Log.e("participateMemberList", "실패")
            }
        })
    }

    /**
     * 신청한 사람 확인 로직------------------------------------------------------------------------------------
     */
    private fun waitMemberList(boardId: Long) {
        boardApi.getMemberBoardWait(boardId).enqueue(object : Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {

                Log.d("GET waitMemberList ALL", response.body().toString())
                // 받아온 리스트 approvalMemberList 안에 넣기
                approvalMemberList.clear()
                response.body()?.memberBoardResponseList?.let { it -> approvalMemberList.addAll(it) }


                // 신청자 목록
                approvalMemberRVAdapter = ApprovalMemberRVAdapter(approvalMemberList)
                binding.rvApprovalMember.adapter = approvalMemberRVAdapter
                binding.rvApprovalMember.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }

            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                // 실패
                Log.e("waitMemberList", "실패")
            }

        })
    }

    /**
     * 신청하기 ------------------------------------------------------------------------------------
     */
    private fun joinMemberBoard(memberBoardApprovalRequest : MemberBoardApprovalRequest) {
        AlertDialog.Builder(this)
            .setTitle("참여하기")
            .setMessage("현재 게시물의 팀원으로 참여를 신청하시겠습니까?")
            .setPositiveButton("예") { dialog, id ->
                boardApi.postMemberBoardCreate(memberBoardApprovalRequest).enqueue(object :
                    Callback<MemberBoardResponse> {
                    override fun onResponse(
                        call: Call<MemberBoardResponse>,
                        response: Response<MemberBoardResponse>
                    ) {
                        Log.d("GET joinMemberBoard ALL", response.body().toString())

                        memberBoardApprovalRequest.boardId?.let { waitMemberList(it) }
                    }

                    override fun onFailure(call: Call<MemberBoardResponse>, t: Throwable) {
                        // 실패
                        Log.e("joinMemberBoard", "실패")
                    }

                })
            }
            .setNegativeButton("아니오") { dialog, id ->

            }
            .show()

    }

}