@file:JvmName("InternetUtils")

package cc.woverflow.onecore.utils

import com.google.gson.JsonElement
import gg.essential.api.utils.WebUtil
import gg.essential.universal.UDesktop
import java.io.File
import java.io.IOException
import java.net.URI

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
 * Download a file from the internet.
 *
 * @param url file location
 * @param file location to save the file
 * @return Whether downloading succeeded.
 */
@JvmOverloads
fun WebUtil.downloadToFileSafe(url: String, file: File, userAgent: String = "Mozilla/4.76 (OneCore)"): Boolean {
    return try {
        downloadToFile(url, file, userAgent)
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

/**
 * Open a website URL in the user's web browser.
 * @param url website URL
 * @return Whether opening succeeded.
 */
fun UDesktop.browseURL(url: String): Boolean = browse(URI.create(url))