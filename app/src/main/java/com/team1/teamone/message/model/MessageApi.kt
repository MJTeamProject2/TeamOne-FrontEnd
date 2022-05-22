package com.team1.teamone.message.model

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MessageApi {
    @POST("/messages")
    fun postMessage(
        @Body messageRequest: MessageRequest
    ): Call<MessageResponse>

    @POST("/room")
    fun postMessageRoom(
        @Body messageRoomRequest: MessageRoomRequest
    ): Call<MessageRoomResponse>

    @GET("/messages/message-room/{messageRoomId}")
    fun getMessageList(
        @Path("messageRoomId") messageRoomId : Long
    ): Call<MessageListResponse>

    @GET("/room/all/{receiverId}")
    fun getMessageRoomList(
        @Path("receiverId") receiverId : Long
    ): Call<MessageRoomListResponse>
}