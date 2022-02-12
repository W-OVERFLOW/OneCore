package cc.woverflow.wcore.utils

import com.google.gson.JsonObject
import gg.essential.api.utils.WebUtil
import gg.essential.universal.UDesktop
import gg.essential.vigilance.Vigilant
import java.io.File
import java.io.IOException

@Deprecated("", ReplaceWith("sequences.any { it != null && this.contains(it, true) }"))
fun String.containsAny(vararg sequences: CharSequence?) = sequences.any { it != null && this.contains(it, true) }

@Deprecated("Replaced by JsonUtils", ReplaceWith("JsonUtils.asJsonElement.asJsonObject"))
fun String.asJsonObject() = asJsonElement().asJsonObject

@Deprecated("Replaced by InternetUtils", ReplaceWith("InternetUtils.fetchJsonElement.asJsonObject"))
@Throws(IOException::class)
fun WebUtil.fetchJson(url: String): JsonObject = fetchJsonElement(url).asJsonObject

@Deprecated("Replaced by InternetUtils", ReplaceWith("InternetUtils.downloadToFileSafe"))
fun WebUtil.downloadToFile(url: String, file: File): Boolean = downloadToFileSafe(url, file)

@Deprecated("Replaced by InternetUtils", ReplaceWith("InternetUtils.browseURL"))
fun UDesktop.browse(url: String) = browseURL(url)

@Deprecated("Replaced by GuiUtils", ReplaceWith("GuiUtils.openScreen"))
fun Vigilant.openGUI() = openScreen()