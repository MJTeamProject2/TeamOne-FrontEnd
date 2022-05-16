package com.team1.teamone.board.model

import com.team1.teamone.util.network.MemberResponse


data class BoardResponse(
    var boardId: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var viewCount: Int? = 0,
    var createdDate: String? = null,
    var updateDate: String? = null,
    var comments: List<CommentResponse>,
    var writer: MemberResponse? = null,
    var boardType: String? = null,
    var boardStatus: String? = null,
    //어필 추가 속성
    var classTitle: String? = null,
    var classDate: String? = null,
    //모집 추가 속성
    var memberCount: Int? = 0,
    var deadLine: String? = null
)


data class CommentResponse(
    var commentId: Long? = null,
    var boardId: Long? = null,
    var writer: MemberResponse? = null,
    var content: String? = null,
    var createdDate: String? = null,
    var updateDate: String? = null
)


data class BoardListResponse(
    val boards: List<BoardResponse>
)

data class FreeBoardRequest(
    val title : String = "",
    val content : String = ""
)

data class AppealBoardRequest(
    var title: String = "",
    var className: String = "",
    var classTime: String = "",
    val content: String = ""
)

data class RecruitmentBoardRequest(
    var title: String = "",
    var personCount: String = "",
    var className: String = "",
    var classTime: String = "",
    var content: String = ""
)