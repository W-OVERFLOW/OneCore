package cc.woverflow.onecore.aether

import com.google.gson.JsonObject
import org.apache.http.HttpStatus
import java.io.IOException
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom

/**
 * Taken from Autotip under the MIT License
 * https://github.com/Semx11/Autotip/blob/master/LICENSE.md
 *
 * @author Semx11
 */
object WebsocketUtils {
    private val RANDOM: SecureRandom = SecureRandom()
    val nextSalt: String
        get() = BigInteger(130, RANDOM).toString(32)

    fun hash(str: String): String {
        return try {
            val digest = digest(str, "SHA-1")
            BigInteger(digest).toString(16)
        } catch (e: NoSuchAlgorithmException) {
            throw IllegalArgumentException(e)
        }
    }

    fun authenticate(token: String, uuid: String, serverHash: String): Int {
        return try {
            val conn: HttpURLConnection = URL("https://sessionserver.mojang.com/session/minecraft/join").openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.doOutput = true
            val obj = JsonObject()
            obj.addProperty("accessToken", token)
            obj.addProperty("selectedProfile", uuid)
            obj.addProperty("serverId", serverHash)
            val jsonBytes: ByteArray = obj.toString().toByteArray(StandardCharsets.UTF_8)
            conn.setFixedLengthStreamingMode(jsonBytes.size)
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.connect()
            conn.outputStream.use { out -> out.write(jsonBytes) }
            conn.responseCode
        } catch (e: IOException) {
            e.printStackTrace()
            HttpStatus.SC_BAD_REQUEST
        }
    }

    @Throws(NoSuchAlgorithmException::class)
    private fun digest(str: String, algorithm: String): ByteArray {
        val md = MessageDigest.getInstance(algorithm)
        val strBytes: ByteArray = str.toByteArray(StandardCharsets.UTF_8)
        return md.digest(strBytes)
    }
}