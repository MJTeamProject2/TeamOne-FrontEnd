package com.team1.teamone.bookmark.model

import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.util.network.BoolResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BookMarkApi {
    @GET("/bookmarks/all")
    fun getAllBookMarks(
    ): Call<BookMarkListResponse>

    @POST("/bookmarks/new/{boardId}")
    fun createBookMark(
        @Path("boardId") boardId : Long
    ): Call<BookMarkResponse>

    @DELETE("/bookmarks/{bookMarkId}")
    fun deleteBookMark(
        @Path("bookMarkId") bookMarkId : Long
    ): Call<BoolResponse>

    @DELETE("/bookmarks/all")
    fun deleteAllBookMarks(): Call<BoolResponse>
}