package com.team1.teamone.board.model

import java.time.LocalDateTime

data class BoardResponse(
    var title: String? = null,
    var content: String? = null,
    var viewCount: Int? = null,
   // var boardType: BoardType? = null,
    var memberCount: Int? = null,
    var classTitle: String? = null,
    var classDate: String? = null
    // var deadline: LocalDateTime = null,
    // var createdAt: LocalDateTime = null
    // var boardStatus: BoardStatus = null, '
    // var comments: List<comment> = null
)

/*data class BoardListResponse(Array<BoardResponse> boards)*/