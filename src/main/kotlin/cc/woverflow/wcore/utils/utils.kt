package cc.woverflow.wcore.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import gg.essential.api.EssentialAPI
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import java.io.File
import java.io.IOException
import java.net.URI

private val parser = JsonParser()

fun String.containsAny(vararg sequences: CharSequence?) = sequences.any { it != null && this.contains(it, true) }

fun String.asJsonObject() = parser.parse(this).asJsonObject

@Throws(IOException::class)
fun WebUtil.fetchJson(url: String): JsonObject = parser.parse(fetchString(url) ?: throw IOException()).asJsonObject

fun WebUtil.downloadToFile(url: String, file: File): Boolean {
    return try {
        downloadToFile(url, file, "Mozilla/4.76 (Essential)")
        true
    } catch (e: IOException) {
        e.printStackTrace()
        false
    }
}

fun UDesktop.browse(url: String) = browse(URI.create(url))

fun Vigilant.openGUI() = Multithreading.runAsync {
    EssentialAPI.getGuiUtil().openScreen(gui())
}