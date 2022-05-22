package com.team1.teamone.caution.model

import com.team1.teamone.util.network.MemberResponse

data class CautionResponse(
    val cautionId : Long,
    val requestMember : MemberResponse,
    val cautionedMember : MemberResponse,
    val createdDate : String
)

data class CautionListResponse(
    val cautions : List<CautionResponse>
)