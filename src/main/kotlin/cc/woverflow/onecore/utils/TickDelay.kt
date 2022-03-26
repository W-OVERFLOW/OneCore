package cc.woverflow.onecore.utils

import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent


fun tick(ticks: Int, block: () -> Unit) = TickDelay(ticks, block)

class TickDelay(var ticks: Int, val block: () -> Unit) {

    init {
        MinecraftForge.EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onTick(event: TickEvent.ClientTickEvent) {
        if (event.phase == TickEvent.Phase.START) {
            if (ticks < 1) {
                block()
                MinecraftForge.EVENT_BUS.unregister(this)
            }
            ticks--
        }
    }
}