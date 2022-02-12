package cc.woverflow.onecore.aether

import xyz.deftu.quicksocket.client.QuickSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

open class AetherClient : QuickSocketClient(
    URI.create("ws://ws-aether.woverflow.cc")
) {
    override fun onConnectionOpened(handshake: ServerHandshake) {
        println("AetherClient: connectionOpened")
    }
}