package com.team1.teamone.board.model

import org.w3c.dom.Comment
import retrofit2.Call
import retrofit2.http.*

interface BoardApi {
    @GET("/boards/all")
    fun getAllBoards(
    ): Call<BoardListResponse>

    @GET("/boards/{boardId}")
    fun getBoard(
        @Path("boardId") boardId : Long
    ): Call<BoardResponse>

    @GET("/boards/written-all/{boardType}")
    fun getAllWrittenBoardsByType(
        @Path("boardType") boardType : String
    ): Call<BoardListResponse>

    @GET("/boards/all/{boardType}")
    fun getAllBoardsByType(
        @Path("boardType") boardType : String
    ): Call<BoardListResponse>

    @GET("/boards/{boardId}/comments")
    fun getAllComments(
        @Path("boardId") boardId: Long
    ): Call<CommentListResponse>

    @POST("/boards/new/free")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/new/wanted")
    fun postWantedBoard(
        @Body wantedBoardRequestForm : WantedBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/new/appeal")
    fun postAppealBoard(
        @Body appealBoardRequestForm : AppealBoardRequest
    ): Call<BoardResponse>

    @PUT("/boards/free/{boardId}")
    fun putFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

    @PUT("/boards/appeal/{boardId}")
    fun putAppealBoard(
        @Body appealBoardRequestForm : AppealBoardRequest
    ): Call<BoardResponse>

    @PUT("/boards/wanted/{boardId}")
    fun putWantedBoard(
        @Body wantedBoardRequestForm : WantedBoardRequest
    ): Call<BoardResponse>

    @DELETE("/boards/{boardId}")
    fun deleteBoardById(
        @Path("boardId") boardId: Long
    ): Call<BoardListResponse>
}