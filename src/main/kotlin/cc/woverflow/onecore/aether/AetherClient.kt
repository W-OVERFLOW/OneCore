package cc.woverflow.onecore.aether


import xyz.deftu.quicksocket.client.QuickSocketClient
import org.java_websocket.handshake.ServerHandshake
import xyz.deftu.quicksocket.common.CloseCode
import java.net.URI

class AetherClient : QuickSocketClient(
    URI.create("ws://ws-aether.aether.codes:4567")
) {
    override fun onConnectionOpened(handshake: ServerHandshake) {
        println("Aether Connection Established")
    }

    override fun onConnectionClosed(code: CloseCode, reason: String, remote: Boolean) {
        println("Aether Connection:$code$reason")
    }

    override fun onErrorOccurred(throwable: Throwable) {
        println("Aether Error")
    }
}