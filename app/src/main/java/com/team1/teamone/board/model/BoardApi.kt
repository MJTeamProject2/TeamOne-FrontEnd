package com.team1.teamone.board.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("/boards/new/free")
    fun postRecruitmentBoard(
        @Body recruitmentBoardRequestForm : RecruitmentBoardRequest
    ): Call<BoardResponse>

    @POST("/boards/new/free")
    fun postAppealBoard(
        @Body appealBoardRequestForm : AppealBoardRequest
    ): Call<BoardResponse>
}