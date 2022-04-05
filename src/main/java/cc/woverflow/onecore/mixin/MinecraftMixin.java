package cc.woverflow.onecore.mixin;
//#if MODERN==0
import cc.woverflow.onecore.OneCore;
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
}
//#else
//$$import cc.woverflow.onecore.utils.KeybindHandler;
//$$import net.minecraft.client.MinecraftClient;
//$$import cc.woverflow.onecore.OneCore;
//$$import net.minecraft.client.RunArgs;
//$$import org.spongepowered.asm.mixin.Mixin;
//$$import org.spongepowered.asm.mixin.injection.At;
//$$import org.spongepowered.asm.mixin.injection.Inject;
//$$import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$
//$$@Mixin(MinecraftClient.class)
//$$public class MinecraftMixin {
//$$    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;onResolutionChanged()V", shift = At.Shift.AFTER))
//$$    private void initOneCore(RunArgs runArgs, CallbackInfo ci) {
//$$        OneCore.INSTANCE.init();
//$$    }
//$$
//$$    @Inject(method = "handleInputEvents", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;handleBlockBreaking(Z)V"))
//$$    private void onKeyPress(CallbackInfo ci) {
//$$        KeybindHandler.INSTANCE.onKeyboardInput();
//$$    }
//$$}
//#endif