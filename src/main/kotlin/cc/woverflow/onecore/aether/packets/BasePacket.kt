package cc.woverflow.onecore.aether.packets

import com.google.gson.JsonObject
import com.google.gson.annotations.Expose
// import codes.aether.aether4j.packets.BasePacket

open class BasePacket constructor(@Expose val type: PacketType, @Expose private val data: JsonObject) // : BasePacket