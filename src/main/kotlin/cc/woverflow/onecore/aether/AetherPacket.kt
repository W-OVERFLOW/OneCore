package cc.woverflow.onecore.aether

import xyz.deftu.quicksocket.common.packets.PacketBase
import com.google.gson.JsonObject

class AetherPacket : PacketBase(
    ""
) {
    override fun onPacketSent(data: JsonObject) {
        data.addProperty("Hello!", "How are you?")
    }

    override fun onPacketReceived(data: JsonObject?) {
        println("I just received a greeting packet! Wow!")
    }
}