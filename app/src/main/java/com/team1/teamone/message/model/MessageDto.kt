package com.team1.teamone.message.model

data class MessageResponse(
    val messageId: Long? = null,
    val senderUserId : String? = null,
    val receiverUserId : String? = null,
    val senderId : Long? = null,
    val receiverId : Long? = null,
    val content : String? = null,
    val createdDate : String? = null,
    val messageRoomId : Long? = null
)

data class MessageRoomResponse(
    val messageRoomId: Long? = null,
    val senderId : Long? = null,
    val receiverId : Long? = null,
    val senderUserId : String? = null,
    val receiverUserId : String? = null,
    val createdDate : String? = null,
    val receiverNickname : String? = null
)

data class MessageRoomListResponse(
    val messageRoomList : List<MessageRoomResponse>
)

data class MessageListResponse(
    val messageList : List<MessageResponse>
)


data class MessageRequest(
    val senderId: Long? = null,
    val receiverId: Long? = null,
    val content: String? = null,
    val messageRoomId: Long? = null
)

data class MessageRoomRequest(
    val senderId : Long? = null,
    val receiverId : Long? = null,
)