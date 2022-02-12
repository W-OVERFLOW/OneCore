package cc.woverflow.onecore.aether

import com.philjay.jwt.JWT


open class AetherProtocol @JvmOverloads constructor(
    token: JWT,
    val encrypted: Boolean,
    headers: Map<String, String> = mapOf()
) {
    final fun decryptToken(message: String, token: JWT) {
        // do a thing
    }

}