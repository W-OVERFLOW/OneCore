package cc.woverflow.onecore.utils

import cc.woverflow.onecore.events.TickEvent
import cc.woverflow.onecore.events.eventBus
import gg.essential.lib.kbrewster.eventbus.Subscribe

class TickDelay(var ticks: Int, val block: () -> Unit) {

    init {
        eventBus.register(this)
    }

    @Subscribe
    fun onTick(event: TickEvent) {
        if (ticks < 1) {
            block()
            eventBus.unregister(this)
        }
        ticks--
    }
}