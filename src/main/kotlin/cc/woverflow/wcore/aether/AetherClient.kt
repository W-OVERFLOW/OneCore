package cc.woverflow.wcore.aether

import cc.woverflow.wcore.WCore
import java.net.URI
import java.net.URISyntaxException
import java.util.Map
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.handshake.ServerHandshake

class AetherClient(uri: String) : WebSocketClient(URI.create(uri)) {
  fun awaitConnection(): Boolean {
    // await connection
  }
  
  override fun onOpen(handshakedata: ServerHandshake) {
    // connection established
  }
  
  override fun onMessage(message: String) {
    println(message)
  }
  
  override fun onClose(code: Int, reason: String?, remote: Boolean) {
        // connection closed
  }

    override fun onError(ex: Exception) {
        // error 
  }
}
