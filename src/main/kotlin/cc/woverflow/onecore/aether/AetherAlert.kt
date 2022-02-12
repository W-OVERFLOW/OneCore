package cc.woverflow.onecore.aether

import cc.woverflow.onecore.aether.AetherClient
import cc.woverflow.onecore.utils.sendBrandedNotification

/*
    AetherAlert: Receives an alert from an AetherClient.
    Used for manual updates, important announcements, etc.
 */
object AetherAlert : AetherClient() {
    fun init() {
        connect()
    }

    var wasAlreadyAlerted = false

    override fun onMessageReceived(message: String) {
        if (!wasAlreadyAlerted) {
            sendBrandedNotification(
                "OneCore",
                "Alert from developers: $message"
            )
            wasAlreadyAlerted = true
        }
    }
}