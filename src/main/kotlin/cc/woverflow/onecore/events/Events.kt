@file:Suppress("RedundantEmptyInitializerBlock")

package cc.woverflow.onecore.events

import gg.essential.lib.kbrewster.eventbus.eventbus
import gg.essential.lib.kbrewster.eventbus.invokers.LMFInvoker
//#if FABRIC==1
//$$ import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
//#endif

val eventBus = eventbus {
    invoker { LMFInvoker() }
}

open class Event

open class CancellableEvent(cancelled: Boolean = false) : Event()

class TickEvent : Event()

object Events {

    init {
        //#if FABRIC==1
        //$$ ClientTickEvents.START_CLIENT_TICK.register { eventBus.post(TickEvent()) }
        //#endif
    }
}