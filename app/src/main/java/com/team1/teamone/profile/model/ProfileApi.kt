package com.team1.teamone.profile.model

import com.team1.teamone.board.model.BoardListResponse
import com.team1.teamone.util.network.MemberResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ProfileApi {

    @GET("/users/{id}")
    fun getMember(
        @Path("id") Id : String
    ): Call<MemberResponse>

}