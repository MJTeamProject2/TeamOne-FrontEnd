package com.team1.teamone.board.model

import com.team1.teamone.caution.model.CautionResponse
import com.team1.teamone.util.network.MemberResponse


data class BoardResponse(
    var boardId: Long? = null,
    var title: String? = null,
    var content: String? = null,
    var viewCount: Int? = 0,
    var createdDate: String? = null,
    var updatedDate: String? = null,
    var comments: List<CommentResponse>,
    var writer: MemberResponse? = null,
    var boardType: String? = null,
    var boardStatus: String? = null,
    //어필 추가 속성
    var classTitle: String? = null,
    var classDate: String? = null,
    //모집 추가 속성
    var memberCount: Int? = 0,
    val currentMemberCount: Int? =0,
    var deadLine: String? = null
)


data class CommentResponse(
    var commentId: Long? = null,
    var boardId: Long? = null,
    var writer: MemberResponse? = null,
    var content: String? = null,
    var createdDate: String? = null,
    var updatedDate: String? = null
)

data class CommentListResponse(
    val comments: List<CommentResponse>
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
    var classTitle: String = "",
    var classDate: String = "",
    val content: String = ""
)

data class WantedBoardRequest(
    var title: String = "",
    var memberCount: String = "",
    var classTitle: String = "",
    var classDate: String = "",
    var content: String = ""
)

data class CommentRequest(
    var content: String = ""
)

data class MemberBoardResponse(
    val memberBoardId: Long? = null,
    val admission: String? = null,
    val createdDate: String? = null,
    val member: MemberResponse? = null,
    val board : BoardResponse? = null,
    val nickname: String? = null,

)

data class MemberBoardListResponse(
    val memberBoardResponseList: List<MemberBoardResponse>
)

data class MemberBoardApprovalRequest(
    val memberId: Long? = null,
    val boardId: Long? = null
)

data class MemberBoardRequest(
    val memberId : Long? = null,
    val boardId : Long? = null
)