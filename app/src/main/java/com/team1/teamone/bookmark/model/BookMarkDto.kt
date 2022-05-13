package com.team1.teamone.bookmark.model

import com.team1.teamone.board.model.BoardResponse
import com.team1.teamone.util.network.MemberResponse
import java.time.LocalDateTime

data class BookMarkResponse(
    val bookMarkId : Long,
    val writer : MemberResponse,
    val board : BoardResponse,
    val createdAt : String

)

data class BookMarkListResponse(
    val bookMarks : List<BookMarkResponse>
)