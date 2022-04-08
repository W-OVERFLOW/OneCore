//#if FABRIC==1
//$$ package cc.woverflow.onecore.internal.mixin;
//$$
//$$ import net.minecraft.text.ClickEvent;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import cc.woverflow.onecore.internal.hook.ClickEventHook;
//$$
//$$ @Mixin(ClickEvent.class)
//$$ public class ClickEventMixin implements ClickEventHook {
//$$     private boolean isOneCoreEvent;
//$$     private Runnable oneCoreRunnable;
//$$
//$$     @Override
//$$     public boolean isOneCoreEvent() {
//$$         return isOneCoreEvent;
//$$     }
//$$
//$$     @Override
//$$     public Runnable getOneCoreRunnable() {
//$$         return oneCoreRunnable;
//$$     }
//$$
//$$     @Override
//$$     public void setOneCoreEvent(boolean value) {
//$$         isOneCoreEvent = value;
//$$     }
//$$
//$$     @Override
//$$     public void setOneCoreRunnable(Runnable value) {
//$$         oneCoreRunnable = value;
//$$     }
//$$ }
//#endif