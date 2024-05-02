package com.pratapabhang.websocketexample

interface WebsocketInterface {
    fun setStatus(status : Boolean)
    fun addMessage(data : Pair<Boolean, String>)
}