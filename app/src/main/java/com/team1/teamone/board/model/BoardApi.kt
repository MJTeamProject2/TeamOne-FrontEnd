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

    @POST("/boards/new/free")
    fun postFreeBoard(
        @Body freeBoardRequestForm : FreeBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/new/wanted")
    fun postRecruitmentBoard(
        @Body recruitmentBoardRequestForm : RecruitmentBoardRequest
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
        @Body recruitmentBoardRequestForm : RecruitmentBoardRequest
    ): Call<BoardResponse>
}