package com.team1.teamone.home.model


data class BoardSimpleModel(
    val title : String = "",
    val content : String = "",
    val classname : String = "",
    val createdDate : String = ""
)

data class UserSimpleModel(
    val userName : String = "",
    val userInfo : String = ""
)

data class RankSimpleModel(
    val nickname : String = "",
    val point : Int = 0
)
