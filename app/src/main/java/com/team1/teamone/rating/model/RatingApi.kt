package com.team1.teamone.rating.model

import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.util.network.MemberResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RatingApi {

    @POST("/ratings")
    fun postRating(

    ): Call<MemberResponse>

    @GET("/ratings/{memberId}")
    fun getRatingList(
        @Path("memberId") memberId : Long
    ): Call<RatingListResponse>

    @GET("/ratings/rated/{memberId}")
    fun getRatedList(
        @Path("memberId") memberId : Long
    ): Call<RatingListResponse>

}