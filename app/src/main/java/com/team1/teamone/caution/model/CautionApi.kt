package com.team1.teamone.caution.model

import com.team1.teamone.util.network.BoolResponse
import retrofit2.Call
import retrofit2.http.*

interface CautionApi {
    @GET("/cautions/all")
    fun getAllCautions(): Call<CautionListResponse>

    @POST("/cautions/{targetMemberId}")
    fun postCaution(
        @Path("targetMemberId") targetMemberId : Long
    ): Call<CautionResponse>

    @DELETE("/cautions/all")
    fun deleteAllCautions(): Call<BoolResponse>

    @DELETE("/cautions/{cautionedMemberId}")
    fun deleteCaution(
        @Path("cautionedMemberId") cautionedMemberId : Long
    ): Call<BoolResponse>

}