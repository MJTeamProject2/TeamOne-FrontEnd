package com.team1.teamone.network

import com.google.gson.annotations.SerializedName


data class MemberResponse(
    var id: Long? = null,
    var userId: String? = null,
    var password: String? = null,
    var email: String? = null,
    var userName: String? = null,
    var department: String? = null,
    var schoolId: String? = null,
    var phoneNumber: String? = null,
    var nickname: String? = null,
    var stars: Double? = null,
    var points: Int? = null,
    var introduce: String? = null,
    var authToken: String? = null,
    var memberType: String? = null
)

data class MemberResponseWithSession(
    @SerializedName("member")
    var member: List<MemberResponse>,
    @SerializedName("sessionId")
    var sessionId: String? = null
)

data class LoginRequest(
    var userId: String? = null,
    var password: String? = null
)

data class RegisterRequest(
    var userName: String? = null,
    var department: String? = null,
    var schoolId: String? = null,
    var phoneNumber: String? = null,
    var nickname: String? = null,
    var userId: String? = null,
    var password: String? = null,
    var passwordCheck: String? = null,
    var email: String? = null,
    var authToken: String? = null
)

data class BoolResponse(
    @SerializedName("result")
    var result : Boolean? = null
)


data class AuthMailResponse(
    var userEmail : String? = null,
    var authToken: String? = null
)

//data class GetAuthToken(
//    var userEmail : String? = null,
//    var authToken: String? = null
//)

// 서버로 요청 보낼떄 사용하는 Model (Request)
data class FindIdRequest(
    var email : String? = null,
    var schoolId : String? = null
)
