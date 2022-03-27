package cc.woverflow.onecore.websocket.packets

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose

open class BasePacket constructor(@Expose val type: PacketType, @Expose private val data: JsonObject)