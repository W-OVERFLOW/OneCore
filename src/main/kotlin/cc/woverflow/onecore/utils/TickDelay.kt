package cc.woverflow.onecore.utils

import cc.woverflow.onecore.internal.TickEvent
import cc.woverflow.onecore.internal.eventBus
import gg.essential.lib.kbrewster.eventbus.Subscribe


fun tick(ticks: Int, block: () -> Unit) = TickDelay(ticks, block)

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