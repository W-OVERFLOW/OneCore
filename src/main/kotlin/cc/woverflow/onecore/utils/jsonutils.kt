@file:JvmName("JsonUtils")

package cc.woverflow.onecore.utils

import com.google.gson.JsonElement
import com.google.gson.JsonParser

private val parser = JsonParser()

/**
 * Return the string provided as a [JsonElement].
 */
fun String.asJsonElementSafe(): Result<JsonElement> = runCatching { return@runCatching this.asJsonElement() }

/**
 * Return the string provided as a [JsonElement]
 *
 * @return string as a [JsonElement]
 */
fun String.asJsonElement(): JsonElement = parser.parse(this)