@file:JvmName("InternetUtils")

package cc.woverflow.onecore.utils

//#if MODERN==1
//$$ import gg.essential.universal.UChat
//$$ import org.apache.hc.client5.http.classic.methods.HttpGet
//$$ import org.apache.hc.client5.http.config.RequestConfig
//$$ import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
//$$ import org.apache.hc.client5.http.impl.classic.HttpClientBuilder
//$$ import org.apache.hc.client5.http.impl.classic.HttpClients
//$$ import org.apache.hc.core5.http.io.entity.EntityUtils
//$$ import org.apache.hc.core5.http.message.BasicHeader
//$$ import org.apache.hc.core5.util.Timeout
//$$ import org.apache.hc.core5.http.HttpResponse
//$$ import java.io.FileOutputStream
//#endif
import cc.woverflow.onecore.OneCore
import com.google.gson.JsonElement
//#if MODERN==0
import gg.essential.api.utils.WebUtil
//#endif
import gg.essential.universal.UDesktop
import java.io.File
import java.io.IOException
import java.net.URI

/**
 * Stolen from Skytils under AGPLv3
 * https://github.com/Skytils/SkytilsMod/blob/1.x/LICENSE.md
 */
object APIUtil {
    //#if MODERN==1
    //$$ private val builder: HttpClientBuilder =
    //$$     HttpClients.custom().setUserAgent("OneCore/${OneCore.VERSION}")
    //$$         .setConnectionManagerShared(true)
    //$$         .setDefaultHeaders(
    //$$              mutableListOf(
    //$$                 BasicHeader("Pragma", "no-cache"),
    //$$                 BasicHeader("Cache-Control", "no-cache")
    //$$             )
    //$$         )
    //$$         .setDefaultRequestConfig(
    //$$             RequestConfig.custom()
    //$$                 .setConnectTimeout(Timeout.ofSeconds(30))
    //$$                 .setResponseTimeout(Timeout.ofSeconds(30))
    //$$                 .build()
    //$$         )
    //$$         .useSystemProperties()
    //#endif

    fun getString(url: String): String? {
        //#if MODERN==0
        return WebUtil.fetchString(url)
        //#else
        //$$ val client = builder.build()
        //$$ try {
        //$$     client.execute(HttpGet(url)).use { response ->
        //$$         response.entity.use { entity ->
        //$$            val obj = EntityUtils.toString(entity)
        //$$            EntityUtils.consume(entity)
        //$$            return obj
        //$$        }
        //$$     }
        //$$ } catch (ex: Throwable) {
        //$$     UChat.chat("§cOneCore ran into an ${ex::class.simpleName ?: "error"} whilst fetching a resource. See logs for more details.")
        //$$     ex.printStackTrace()
        //$$ } finally {
        //$$     client.close()
        //$$ }
        //$$ return null
        //#endif
    }

    fun download(url: String, file: File): Boolean {
        //#if MODERN==0
        return WebUtil.downloadToFileSafe(url, file)
        //#else
        //$$ val escapedUrl = url.replace(" ", "%20")
        //$$ try {
        //$$     FileOutputStream(file).use { fileOut ->
        //$$         builder.build().execute(HttpGet(escapedUrl)).use { response ->
        //$$             val buffer = ByteArray(1024)
        //$$             var read: Int
        //$$             while (response.entity.content.read(buffer).also { read = it } > 0) {
        //$$                 fileOut.write(buffer, 0, read)
        //$$             }
        //$$         }
        //$$     }
        //$$ } catch (e: Exception) {
        //$$     e.printStackTrace()
        //$$     return false
        //$$ }
        //$$ return true
        //#endif
    }
}

fun APIUtil.getJsonElement(url: String): JsonElement? = getString(url)?.asJsonElementSafe()?.getOrNull()

//#if MODERN==0

/**
 * Fetch a JSON object from the internet.
 *
 * @param url JSON location
 * @return page content as a [JsonElement]
 * @throws IOException if getting the JSON failed
 * @see WebUtil
 */
@Throws(IOException::class)
fun WebUtil.fetchJsonElement(url: String): JsonElement = (fetchString(url) ?: throw IOException()).asJsonElement()

/**
 * Fetch a JSON object from the internet.
 *
 * @param url JSON location
 * @return page content as a [JsonElement], or null if getting the JSON failed.
 * @see fetchJsonElement
 */
fun WebUtil.fetchJsonElementSafe(url: String): JsonElement? = fetchString(url)?.asJsonElementSafe()?.getOrNull()

/**
 * Download a file from the internet.
 *
 * @param url file location
 * @param file location to save the file
 * @return Whether downloading succeeded.
 */
@JvmOverloads
fun WebUtil.downloadToFileSafe(url: String, file: File, userAgent: String = "${OneCore.NAME}/${OneCore.VERSION}, Minecraft/1.8.9 (+https://woverflow.cc)"): Boolean {
    return try {
        downloadToFile(url, file, userAgent)
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

fun WebUtil.downloadToFile(url: String, file: File) = downloadToFile(url, file, "${OneCore.NAME}/${OneCore.VERSION}, Minecraft/1.8.9 (+https://woverflow.cc)")

//#endif

/**
 * Open a website URL in the user's web browser.
 * @param url website URL
 * @return Whether opening succeeded.
 */
fun UDesktop.browseURL(url: String): Boolean = browse(URI.create(url))
