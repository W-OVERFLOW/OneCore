package cc.woverflow.onecore.internal

import gg.essential.lib.kbrewster.eventbus.eventbus
import gg.essential.lib.kbrewster.eventbus.invokers.LMFInvoker
//#if MODERN==0
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent
//#else
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
        //#if MODERN==0
        MinecraftForge.EVENT_BUS.register(this)
        //#else
        //$$ ClientTickEvents.START_CLIENT_TICK.register { eventBus.post(TickEvent()) }
        //$$
    }

    //#if MODERN==0
    @SubscribeEvent
    fun onTick(e: TickEvent.ClientTickEvent) {
        if (e.phase == TickEvent.Phase.START) {
            eventBus.post(TickEvent())
        }
    }
    //#endif
}