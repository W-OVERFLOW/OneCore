package cc.woverflow.onecore.mixin;
//#if MODERN==0

import cc.woverflow.onecore.OneCore;
import cc.woverflow.onecore.internal.EventsKt;
import cc.woverflow.onecore.internal.TickEvent;
import cc.woverflow.onecore.utils.KeybindHandler;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/client/FMLClientHandler;finishMinecraftLoading()V", shift = At.Shift.AFTER))
    private void initOneCore(CallbackInfo ci) {
        OneCore.INSTANCE.init();
    }

    @Inject(method = "runTick", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/FMLCommonHandler;fireKeyInput()V"))
    private void onKeyPress(CallbackInfo ci) {
        KeybindHandler.INSTANCE.onKeyboardInput();
    }

    @Inject(method = "runTick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        EventsKt.getEventBus().post(new TickEvent());
    }
}
//#else
//$$import cc.woverflow.onecore.utils.KeybindHandler;
//$$import cc.woverflow.onecore.OneCore;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.injection.At;
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.Coerce;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
    //#if FABRIC==1
    //$$ @Mixin(net.minecraft.client.MinecraftClient.class)
    //#else
    //$$ @Mixin(net.minecraft.client.Minecraft.class)
    //#endif
//$$public class MinecraftMixin {
//$$    @Inject(method = "<init>", at = @At(value = "INVOKE",
    //#if FABRIC==1
    //$$ target = "Lnet/minecraft/client/MinecraftClient;onResolutionChanged()V",
    //#else
    //$$ target = "Lnet/minecraft/client/Minecraft;resizeDisplay()V",
    //#endif
//$$    shift = At.Shift.AFTER))
//$$    private void initOneCore(@Coerce Object runArgs, CallbackInfo ci) {
//$$        OneCore.INSTANCE.init();
//$$    }
//$$
//$$    @Inject(
    //#if FABRIC==1
    //$$ method = "handleInputEvents",
    //#else
    //$$ method = "handleKeybinds",
    //#endif
//$$ at = @At(value = "INVOKE",
    //#if FABRIC==1
    //$$ target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"))
    //#else
    //$$ target = "Lnet/minecraft/client/Minecraft;continueAttack(Z)V"))
    //#endif
//$$    private void onKeyPress(CallbackInfo ci) {
//$$        KeybindHandler.INSTANCE.onKeyboardInput();
//$$    }
//$$}
//#endif

