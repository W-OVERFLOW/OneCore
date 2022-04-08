//#if FABRIC==1
//$$ package cc.woverflow.onecore.internal.mixin;
//$$ 
//$$ import net.minecraft.client.gui.screen.Screen;
//$$ import net.minecraft.text.Style;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import cc.woverflow.onecore.internal.hook.ClickEventHook;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ 
//$$ @Mixin(Screen.class)
//$$ public class ScreenMixin {
//$$     @Inject(method = "handleTextClick", at = @At("HEAD"), cancellable = true)
//$$     private void onTextClick(Style style, CallbackInfoReturnable<Boolean> cir) {
//$$         if (style != null && style.getClickEvent() != null) {
//$$             ClickEventHook event = ((ClickEventHook) style.getClickEvent());
//$$             if (event.isOneCoreEvent()) {
//$$                 event.getOneCoreRunnable().run();
//$$             }
//$$         }
//$$     }
//$$ }
//#endif