package cc.woverflow.onecore.websocket.packets

import com.google.gson.JsonObject

class GreetingPacket(val uuid: String) : BasePacket(type = PacketType.GREETING, data = JsonObject().also { it.addProperty("uuid", uuid) })