package com.team1.teamone.board.model

import androidx.annotation.BoolRes
import com.team1.teamone.util.network.BoolResponse
import com.team1.teamone.util.network.MemberResponse
import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.http.*

interface BoardApi {
    @GET("/boards/all")
    fun getAllBoards(
    ): Call<BoardListResponse>

    @GET("/boards/{boardId}")
    fun getBoard(
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    @GET("/boards/written-all/{boardType}")
    fun getAllWrittenBoardsByType(
        @Path("boardType") boardType: String
    ): Call<BoardListResponse>

    @GET("/boards/all/{boardType}")
    fun getAllBoardsByType(
        @Path("boardType") boardType: String
    ): Call<BoardListResponse>

    @GET("/boards/{boardId}/comments")
    fun getAllComments(
        @Path("boardId") boardId: Long
    ): Call<CommentListResponse>

    @POST("/boards/free")
    fun postFreeBoard(
        @Body freeBoardRequestForm: FreeBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/wanted")
    fun postWantedBoard(
        @Body wantedBoardRequestForm: WantedBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/appeal")
    fun postAppealBoard(
        @Body appealBoardRequestForm: AppealBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/{boardId}/comments")
    fun postComment(
        @Body commentRequestForm: CommentRequest,
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    @PUT("/boards/free/{boardId}")
    fun putFreeBoard(
        @Body freeBoardRequestForm: FreeBoardRequest,
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    @PUT("/boards/appeal/{boardId}")
    fun putAppealBoard(
        @Body appealBoardRequestForm: AppealBoardRequest,
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    @PUT("/boards/wanted/{boardId}")
    fun putWantedBoard(
        @Body wantedBoardRequestForm: WantedBoardRequest,
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    @PUT("/comments/{commentId}")
    fun putComment(
        @Body commentRequestForm: CommentRequest,
        @Path("commentId") commentId: Long
    ): Call<BoardResponse>

    @DELETE("/boards/{boardId}")
    fun deleteBoardById(
        @Path("boardId") boardId: Long
    ): Call<BoolResponse>


    // 게시물 검색
    @GET("/boards/search/{searchWay}/{keyword}")
    fun getSearching(
        @Path("searchWay") searchWay: String,
        @Path("keyword") keyword: String
    ): Call<BoardListResponse>

    @DELETE("/comments/{commentId}")
    fun deleteCommentById(
        @Path("commentId") commentId: Long
    ): Call<BoolResponse>

    // 참여자 정보 가져오기
    @GET("/member-board/ok/{boardId}")
    fun getMemberBoardApproval(
        @Path("boardId") boardId: Long
    ): Call<MemberBoardListResponse>


    // 신청한 유저 정보 가져오기
    @GET("/member-board/wait/{boardId}")
    fun getMemberBoardWait(
        @Path("boardId") boardId: Long
    ): Call<MemberBoardListResponse>

    @POST("/member-board")
    fun postMemberBoardCreate(
        @Body memberBoardApprovalRequest: MemberBoardApprovalRequest
    ): Call<MemberBoardResponse>

    // 승인 하기
    @POST("member-board/{memberBoardId}")
    fun postApprovalMemberBoard(
        @Path("memberBoardId") memberBoardId: Long
    ): Call<MemberBoardResponse>

    // 거부 하기
    @POST("member-board/no/{memberBoardId}")
    fun postNoApprovalMemberBoard(
        @Path("memberBoardId") memberBoardId: Long
    ): Call<MemberBoardResponse>

    // 종료 하기
    @GET("/boards/finish/{boardId}")
    fun getFinishBoard(
        @Path("boardId") boardId: Long
    ): Call<BoardResponse>

    // 종료된 목록 가져 오기(자기가 가입된 그룹만(MemberBoard))
    @GET("/member-board/approval/member/{memberId}")
    fun getFinishBoardList(
        @Path("memberId") memberId: Long
    ): Call<MemberBoardListResponse>


    // 종료된 맴버 리스트 가져오기
    @POST("/member-board/finish")
    fun postFinishMemberList(
        @Body memberBoardRequest: MemberBoardRequest
    ): Call<MemberBoardListResponse>


}