package com.team1.teamone.board.view.activity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.get
import androidx.core.view.isVisible
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
import com.team1.teamone.profile.view.MemberInfoActivity
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
    var boardIdGlobal : Long = 0

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
        boardIdGlobal = boardId.toLong()
        val writerUserId = intent.getLongExtra("detailUserId", 0)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_detail)


        // ?????? ????????? ??????
        binding.profileGo1.setOnClickListener {
            Log.e("profileGo1 writerUserId", writerUserId.toString())
            val intent = Intent(applicationContext, MemberInfoActivity::class.java)
            intent.putExtra("targetId", writerUserId.toString())
            startActivity(intent)
        }


        binding.detailTitle.text = title
        binding.tvDetailContent.text = content
        binding.tvDetailViewCount.text = "?????????:" + viewCount.toString()
        binding.tvUpdateDate.text = updateDate
        binding.tvDetailWriter.text = writer
        Log.e("boardType",boardType.toString())
        // ????????? ??????
        if(boardType.toString() == "FREE") {
            binding.tvDetailClassDate.text = null
            binding.tvDetailClassTitle.text = null
            binding.tvCurrentPerson.text = null
            binding.tvDetailMemberCount.text = null
            binding.tvDetailCurrentMemberCount.text = null
            binding.tvSlash.text = null
            binding.btnJoinBoard.visibility = INVISIBLE
            binding.tvApplyPerson.text = null
            binding.tvParticipantPerson.text = null

            if(userid == writerUserId.toString()) {
                binding.btnBoardDetailUpdateBoard.setOnClickListener {
                    val intent = Intent(applicationContext, UpdateFreeBoardActivity::class.java)
                    intent.putExtra("updateBoardId", boardId)
                    startActivity(intent)
                    binding.approvalList.visibility = VISIBLE
                    binding.btnBoardDetailEnd.text = "?????? ??????"
                    binding.btnBoardDetailEnd.isEnabled = true
                }
            } else {
                binding.btnBoardDetailUpdateBoard.visibility = INVISIBLE
                binding.approvalList.visibility = INVISIBLE
                binding.btnBoardDetailEnd.text = "?????? ???"
                binding.btnBoardDetailEnd.isEnabled = false
            }
            binding.btnBoardDetailEnd.visibility = INVISIBLE
            binding.approvalList.visibility = INVISIBLE
            binding.btnBoardDetailEnd.text = "?????? ???"
            binding.btnBoardDetailEnd.isEnabled = false
        }

        if(boardType.toString() == "APPEAL") {
            binding.tvCurrentPerson.text = null
            binding.tvDetailMemberCount.text = null
            binding.tvDetailCurrentMemberCount.text = null
            binding.tvSlash.text = null
            binding.btnJoinBoard.visibility = INVISIBLE
            binding.tvApplyPerson.text = null
            binding.tvParticipantPerson.text = null

            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            binding.tvDetailClassTitle.text = classTitle
            binding.tvDetailClassDate.text = classDate

            // ????????? ??????
            if(userid == writerUserId.toString()) {
                binding.btnBoardDetailUpdateBoard.setOnClickListener {
                    val intent = Intent(applicationContext, UpdateAppealBoardActivity::class.java)
                    intent.putExtra("updateBoardId", boardId)
                    startActivity(intent)
                    binding.approvalList.visibility = VISIBLE
                    binding.btnBoardDetailEnd.text = "?????? ??????"
                    binding.btnBoardDetailEnd.isEnabled = true

                }
            } else {
                binding.btnBoardDetailUpdateBoard.visibility = INVISIBLE
                binding.approvalList.visibility = INVISIBLE
                binding.btnBoardDetailEnd.text = "?????? ???"
                binding.btnBoardDetailEnd.isEnabled = false
            }
            binding.btnBoardDetailEnd.visibility = INVISIBLE
            binding.approvalList.visibility = INVISIBLE
            binding.btnBoardDetailEnd.text = "?????? ???"
            binding.btnBoardDetailEnd.isEnabled = false
        }

        if(boardType.toString() == "WANTED") {
            val classTitle = intent.getStringExtra("detailClassTitle")
            val classDate = intent.getStringExtra("detailClassDate")
            val memberCount = intent.getIntExtra("detailMemberCount", 0)
            val currentMemberCount = intent.getIntExtra("detailCurrentMemberCount", 0)

            if(memberCount <= currentMemberCount){
                binding.btnJoinBoard.visibility = INVISIBLE
                binding.tvCurrentPerson.setTextColor(Color.RED)
                binding.tvDetailCurrentMemberCount.setTextColor(Color.RED)
                binding.tvSlash.setTextColor(Color.RED)
                binding.tvDetailMemberCount.setTextColor(Color.RED)
            } else {
                binding.tvCurrentPerson.setTextColor(Color.BLUE)
                binding.tvDetailCurrentMemberCount.setTextColor(Color.BLUE)
                binding.tvSlash.setTextColor(Color.BLUE)
                binding.tvDetailMemberCount.setTextColor(Color.BLUE)
            }

            if(currentMemberCount == 1){
                binding.tvParticipantPerson.visibility = INVISIBLE
            }

            if(approvalMemberList.count() == 1) {
                binding.tvApplyPerson.visibility = INVISIBLE
            }

            binding.tvDetailMemberCount.text = memberCount.toString()
            binding.tvDetailClassDate.text = classDate
            binding.tvDetailClassTitle.text = classTitle
            binding.tvDetailCurrentMemberCount.text = currentMemberCount.toString()

            // ????????? ??????
            if(userid == writerUserId.toString()) {
                binding.btnBoardDetailUpdateBoard.setOnClickListener {
                    val intent = Intent(applicationContext, UpdateWantedBoardActivity::class.java)
                    intent.putExtra("updateBoardId", boardId)
                    startActivity(intent)
                    binding.approvalList.visibility = VISIBLE
                    binding.btnBoardDetailEnd.text = "?????? ??????"
                    binding.btnBoardDetailEnd.isEnabled = true
                }
            } else {
                binding.btnBoardDetailUpdateBoard.visibility = INVISIBLE
                binding.approvalList.visibility = INVISIBLE
                binding.btnBoardDetailEnd.text = " "
                binding.btnBoardDetailEnd.isEnabled = false
            }

        }

        drawCommentList(boardId)
        updateBookMarkStar(boardId)

//        Log.e("hihi","${userid}")
//        Log.e("hihi","${writerUserId}")

        if(userid == writerUserId.toString()) {
            // ????????? ?????? ??????
            binding.btnBoardDetailDeleteBoard.setOnClickListener {
                deleteBoard(boardId)
            }
        } else {
            binding.btnBoardDetailDeleteBoard.visibility = INVISIBLE
        }

        //????????? ?????? ??????
        binding.btnCommentInputBtn.setOnClickListener {
            val commentContent = edt_commentEdit.text.toString()
            val request = CommentRequest(commentContent)
            createComment(request,boardId)
            binding.edtCommentEdit.setText("")
        }

        //????????? ?????? ??????

        // ????????? (???????????? ??????) ??????
        binding.btnBoardDetailBookMark.setOnClickListener {
            toggleBookMark(boardId)
        }
        Log.d("waitMemberList", boardId.toString())

        participateMemberList(boardId)
        waitMemberList(boardId)

        // ????????????
        binding.btnJoinBoard.setOnClickListener {
            joinMemberBoard(MemberBoardApprovalRequest(userid.toLong() , boardId))
        }

        // ????????????
        binding.btnBoardDetailEnd.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("?????? ??????")
                .setMessage("?????? ????????? ?????????????????????????")
                .setPositiveButton("???") { dialog, id ->
                    putFinishBoard(boardId)
                }.setNegativeButton("?????????") { dialog, id ->
                }
                .show()
        }
    }

    /**
     * ????????? ?????? ????????????--------------------------------------------------------------------------------
     */
    private fun putFinishBoard(boardId: Long) {
        boardApi.putFinishBoard(boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(
                call: Call<BoardResponse>,
                response: Response<BoardResponse>
            ) {
                //
                Toast.makeText(this@BoardDetailActivity, "?????? ????????? ??????????????????", Toast.LENGTH_SHORT).show()
                binding.btnBoardDetailEnd.visibility = INVISIBLE
                binding.btnJoinBoard.visibility = INVISIBLE

            }

            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                //
            }
        })
    }


    /**
     *  ????????? ?????? ??????------------------------------------------------------------------------------------
     */
    // ?????? ????????? ?????? ???????????? ??????, ???????????? ??? ??????????????? ???????????? ???????????? ????????????
    private fun updateBookMarkStar(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                Log.e("???????????? ????????? ??????", "??????")
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // ????????? ????????? ???????????? ?????? ???????????? ?????? ??? ??????
                for (bookMark in bookMarkList) {
                    // ????????? ?????? ??????
                    if (bookMark.board.boardId == boardId) {
                        binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                        return
                    }
                }
                // ????????? ???????????? ??????
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // ??????
                Log.e("????????? ?????? ????????? ????????? ?????? ??????", "??????")
            }

        })
    }

    // ????????? ??????
    private fun postBookMark(boardId: Long) {
        bookMarkApi.createBookMark(boardId).enqueue(object : Callback<BookMarkResponse> {
            override fun onResponse(
                call: Call<BookMarkResponse>,
                response: Response<BookMarkResponse>
            ) {
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_24)
                Toast.makeText(this@BoardDetailActivity, "??????????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure(call: Call<BookMarkResponse>, t: Throwable) {
                // ??????
                Log.e("????????? ?????? ????????? ?????? ??????", "??????")
            }
        })
    }

    // ????????? ??????
    private fun deleteBookMark(bookMarkId: Long) {
        bookMarkApi.deleteBookMark(bookMarkId).enqueue(object : Callback<BoolResponse>{
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                binding.btnBoardDetailBookMark.setImageResource(R.drawable.ic_baseline_star_border_24)
                Toast.makeText(this@BoardDetailActivity, "???????????? ???????????? ??????????????????", Toast.LENGTH_SHORT).show()
            }
            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // ??????
                Log.e("????????? ?????? ????????? ?????? ??????", "??????")
            }
        })
    }

    // ????????? ??????
    private fun toggleBookMark(boardId: Long) {
        val bookMarkList = mutableListOf<BookMarkResponse>()
        bookMarkApi.getAllBookMarks().enqueue(object : Callback<BookMarkListResponse> {
            override fun onResponse(
                call: Call<BookMarkListResponse>,
                response: Response<BookMarkListResponse>
            ) {
                response.body()?.bookMarks?.let { it -> bookMarkList.addAll(it) }
                // ????????? ????????? ???????????? ?????? ???????????? ?????? ??? ??????
                for (bookMark in bookMarkList) {
                    // ????????? ????????? ??????
                    if (bookMark.board.boardId == boardId) {
                        deleteBookMark(bookMark.bookMarkId)
                        return
                    }
                }
                // ????????? ????????? ??????
                postBookMark(boardId)
                return
            }
            override fun onFailure(call: Call<BookMarkListResponse>, t: Throwable) {
                // ??????
                Log.e("????????? ?????? ????????? ????????? ?????? ??????", "??????")
            }

        })
    }
    /**
     *  ????????? ?????? ?????? ------------------------------------------------------------------------------------
     */
    // ????????? ?????????

    private fun createComment(request: CommentRequest,boardId: Long) {
        boardApi.postComment(request,boardId).enqueue(object : Callback<BoardResponse> {
            override fun onResponse(call: Call<BoardResponse>, response: Response<BoardResponse>) {
                Log.d("auth", RetrofitClient.getAuth())
                if (response.body() == null) {
                    Log.d("comment1???", "blank")
                    return
                } else {
                    drawCommentList(boardId)
                    Log.d("comment1???", "blank")
                    Log.d("log", "success")
                }
            }
            override fun onFailure(call: Call<BoardResponse>, t: Throwable) {
                // ??????
                Log.d("log", "fail")
            }
        })
    }

    private fun drawCommentList(boardId: Long) {
        boardApi.getAllComments(boardId).enqueue(object : Callback<CommentListResponse> {
            override fun onResponse(call: Call<CommentListResponse>, response: Response<CommentListResponse>) {
                // ????????? ????????? boardDataList ?????? ??????
                commentDataList.clear()
                response.body()?.comments?.let { it -> commentDataList.addAll(it) }

                // ?????????????????? - ????????? ??????
                commentAdapter = CommentAdapter(commentDataList)
                binding.rvCommentList.adapter = commentAdapter

                // ?????????????????? ?????? ??????
                binding.rvCommentList.layoutManager = LinearLayoutManager(
                    this@BoardDetailActivity,
                    LinearLayoutManager.VERTICAL,
                    false
                )

                binding.rvCommentList?.scrollToPosition(commentDataList.size -1)
                commentAdapter.setItemClickListener(object : CommentAdapter.OnItemClickListener {
                    override fun onClick(v: View, position: Int) {
                        deleteComment(position, boardId)
                    }
                })
            }


            override fun onFailure(call: Call<CommentListResponse>, t: Throwable) {
                // ??????
                Log.d("????????? ?????? ??????", "????????? ??????")
            }
        })
    }



    private fun deleteComment(position: Int, boardId: Long) {
        AlertDialog.Builder(this)
            .setTitle("?????? ??????")
            .setMessage("?????? ????????? ?????????????????????????")
            .setPositiveButton("???") { dialog, id ->
                deleteRequestToServer(position, boardId)
            }.setNegativeButton("?????????") { dialog, id ->

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
                    // ??????
                    Log.d("?????? ?????? ??????", "?????? ?????? ?????? ??????")
                }
            })
        }
    }


    /**
     * ????????? ?????? ??????------------------------------------------------------------------------------------
     */
    // ????????? ?????? ?????? ???????????????
    private fun deleteBoard(boardId: Long) {
        AlertDialog.Builder(this)
            .setTitle("????????? ??????")
            .setMessage("?????? ???????????? ?????? ???????????????????")
            .setPositiveButton("???") { dialog, id ->
                deleteRequestToServer(boardId) // ????????? ????????? ?????? ??????
            }
            .setNegativeButton("?????????") { dialog, id ->

            }
            .show()
    }

    // ????????? ????????? ?????? ??????
    private fun deleteRequestToServer(boardId: Long) {
        boardApi.deleteBoardById(boardId).enqueue(object : Callback<BoolResponse> {
            override fun onResponse(call: Call<BoolResponse>, response: Response<BoolResponse>) {
                Toast.makeText(this@BoardDetailActivity, "???????????? ?????? ????????????", Toast.LENGTH_SHORT).show()
                val intent = Intent(applicationContext, HomeActivity::class.java)
                startActivity(intent)
            }

            override fun onFailure(call: Call<BoolResponse>, t: Throwable) {
                // ??????
                Log.d("????????? ?????? ?????? ??????", "????????? ?????? ??????")
            }
        })
    }


    /**
     * ????????? ?????? ?????? ??????------------------------------------------------------------------------------------
     */
    private fun participateMemberList(boardId : Long) {
        boardApi.getMemberBoardApproval(boardId).enqueue(object :
            Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {

                Log.d("GET participateMemberList ALL", response.body().toString())

                // ????????? ????????? participatingMemberList ?????? ??????
                Log.d("participateMemberList", participatingMemberList.toString())
                participatingMemberList.clear()
                response.body()?.memberBoardResponseList?.let { it -> participatingMemberList.addAll(it) }

                // ????????? ??????
                participateAdapter = ParticipatingMemberRVAdapter(participatingMemberList)
                binding.rvParticipatingMember.adapter = participateAdapter
                binding.rvParticipatingMember.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            }
            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                // ??????
                Log.e("participateMemberList", "??????")
            }
        })
    }

    /**
     * ????????? ?????? ?????? ??????------------------------------------------------------------------------------------
     */
    private fun waitMemberList(boardId: Long) {
        boardApi.getMemberBoardWait(boardId).enqueue(object : Callback<MemberBoardListResponse> {
            override fun onResponse(
                call: Call<MemberBoardListResponse>,
                response: Response<MemberBoardListResponse>
            ) {

                Log.d("GET waitMemberList ALL", response.body().toString())
                // ????????? ????????? approvalMemberList ?????? ??????
                approvalMemberList.clear()
                response.body()?.memberBoardResponseList?.let { it -> approvalMemberList.addAll(it) }


                // ????????? ??????
                approvalMemberRVAdapter = ApprovalMemberRVAdapter(approvalMemberList)
                binding.rvApprovalMember.adapter = approvalMemberRVAdapter
                binding.rvApprovalMember.layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, false)

            }

            override fun onFailure(call: Call<MemberBoardListResponse>, t: Throwable) {
                // ??????
                Log.e("waitMemberList", "??????")
            }

        })
    }

    /**
     * ???????????? ------------------------------------------------------------------------------------
     */
    private fun joinMemberBoard(memberBoardApprovalRequest : MemberBoardApprovalRequest) {
        AlertDialog.Builder(this)
            .setTitle("????????????")
            .setMessage("?????? ???????????? ???????????? ????????? ?????????????????????????")
            .setPositiveButton("???") { dialog, id ->
                boardApi.postMemberBoardCreate(memberBoardApprovalRequest).enqueue(object :
                    Callback<MemberBoardResponse> {
                    override fun onResponse(
                        call: Call<MemberBoardResponse>,
                        response: Response<MemberBoardResponse>
                    ) {

                        participateMemberList(boardIdGlobal)
                        Log.d("GET joinMemberBoard ALL", response.body().toString())

                        memberBoardApprovalRequest.boardId?.let { waitMemberList(it) }
                    }

                    override fun onFailure(call: Call<MemberBoardResponse>, t: Throwable) {
                        // ??????
                        Log.e("joinMemberBoard", "??????")
                    }

                })
            }
            .setNegativeButton("?????????") { dialog, id ->

            }
            .show()

    }

}