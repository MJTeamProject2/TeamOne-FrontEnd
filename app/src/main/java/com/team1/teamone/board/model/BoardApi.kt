package com.team1.teamone.board.model

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

    @POST("/boards/new/free")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/new/wanted")
    fun postRecruitmentBoard(
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
    fun putRecruitmentBoard(
        @Body wantedBoardRequestForm : WantedBoardRequest
    ): Call<BoardResponse>
}