package cc.woverflow.onecore.websocket.packets

import com.google.gson.JsonArray
import com.google.gson.JsonObject

class OutgoingCosmeticsPacket(val purchases: JsonArray) : BasePacket(type = PacketType.OUTGOING_COSMETICS, JsonObject().also { it.add("purchases", purchases) })

class IncomingCosmeticsPacket(val uuid: String) : BasePacket(type = PacketType.INCOMING_COSMETICS, JsonObject().also { it.addProperty("uuid", uuid) })