package com.team1.teamone.board.model

data class BoardResponse(
    var boardId: Long? = null,
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

data class BoardListResponse(
    val boards : ArrayList<BoardResponse> = ArrayList<BoardResponse>()
)