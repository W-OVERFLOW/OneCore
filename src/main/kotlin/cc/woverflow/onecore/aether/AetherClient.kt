package cc.woverflow.onecore.aether

import cc.woverflow.onecore.utils.asJsonElement
import cc.woverflow.onecore.utils.mc
import cc.woverflow.onecore.aether.packets.*
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import gg.essential.api.utils.Multithreading
import org.apache.logging.log4j.LogManager
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit
// import codes.aether.aether4j.ForgeClient
// import codes.aether.aether4j.connections.mcp.Forge
// import codes.aether.aether4j.connections.loom.WLoom
// import codes.aether.aether4j.connections.mcp.MCP
// import codes.aether.aether4j.connections.loom.Mixins
// import codes.aether.aether4j.connections.core.AetherWAPI
// import codes.aether.aether4j.connections.helper.TebexAPI


object AetherClient : WebSocketClient(URI.create("ws://localhost:8887")) {
    var userType: UserType? = null
    private set

    private val GSON = GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
    private val PARSER = JsonParser()
    private var failed = 0
    private val logger = LogManager.getLogger()


    init {
        isTcpNoDelay = true
        isReuseAddr = true
        connectionLostTimeout = 0
    }

    override fun onOpen(handshakedata: ServerHandshake) {
        logger.info("Aether Connection opened | Code: ${handshakedata.httpStatus} | Message: ${handshakedata.httpStatusMessage}")
        send(GreetingPacket(mc.session.playerID))
    }

    override fun onMessage(message: String?) {
        if (!message.isNullOrBlank()) {
            val json = message.asJsonElement().asJsonObject
            println(json)
            val parsedJson = PARSER.parse(message).asJsonObject
            when (PacketType.valueOf(parsedJson["type"].asString)) {
                PacketType.USERTYPE -> {
                    val userTypeJson = GSON.fromJson(parsedJson, UserTypePacket::class.java)
                    userType = userTypeJson.userType
                }
                PacketType.OUTGOING_COSMETICS -> {
                    //GSON.toJson(parsedJson["data"].asJsonObject["purchases"])
                }
                else -> {}
            }
        }
    }

    override fun onMessage(bytes: ByteBuffer) {
        onMessage(StandardCharsets.UTF_8.decode(bytes).toString())
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        when (CloseType.valueOf(code)) {
            CloseType.NORMAL -> {
                logger.info("Closing Aether connection... (reason - $reason)")
            }
            else -> {
                ++failed
                if (failed >= 3) {
                    logger.error("Aether connection failed. (code: $code | reason: $reason)")
                } else {
                    logger.warn("Connection failed $failed times... trying again")
                    Multithreading.schedule(this::reconnectBlocking, 5, TimeUnit.SECONDS)
                }
            }
        }
    }

    fun send(packet: BasePacket) {
        try {
            if (isOpen) {
                send(GSON.toJson(packet).toByteArray(StandardCharsets.UTF_8))
            } else {
                logger.error("Tried to send " + packet.type + " but connection wasn't open!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onError(ex: Exception) {
        ex.printStackTrace()
    }

    override fun connectBlocking(): Boolean {
        return try {
            connectBlocking(30, TimeUnit.SECONDS)
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override fun reconnectBlocking(): Boolean {
        return try {
            super.reconnectBlocking()
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}