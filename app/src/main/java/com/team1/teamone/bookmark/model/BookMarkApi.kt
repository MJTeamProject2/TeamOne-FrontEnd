package com.team1.teamone.bookmark.model

import com.team1.teamone.board.model.BoardListResponse
import retrofit2.Call
import retrofit2.http.GET

interface BookMarkApi {
    @GET("/bookmarks/all")
    fun getAllBookMarks(
    ): Call<BookMarkListResponse>
}