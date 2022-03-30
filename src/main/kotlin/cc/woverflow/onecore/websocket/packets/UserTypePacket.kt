package cc.woverflow.onecore.websocket.packets

import com.google.gson.JsonObject

class UserTypePacket(val userType: UserType): BasePacket(type = PacketType.USERTYPE, data = JsonObject().also { it.addProperty("user_type", userType.name) })