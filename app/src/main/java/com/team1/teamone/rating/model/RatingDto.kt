package com.team1.teamone.rating.model

import android.media.Rating
import com.team1.teamone.util.network.MemberResponse

data class RatingRequest(
    var memberBoardId : Long,
    var ratingMemberId : Long,
    var ratedMemberId : Long,
    var star : Double,
    var badgeId : Long
)

data class RatingResponse(
    var ratingId : Long,
    var memberBoardId : Long,
    var ratingMember : MemberResponse,
    var ratedMember : MemberResponse,
    var star : Double
)

data class RatingListResponse(
    var ratings : List<RatingResponse>
)