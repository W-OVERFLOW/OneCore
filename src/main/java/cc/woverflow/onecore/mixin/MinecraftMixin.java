package cc.woverflow.onecore.mixin;

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
