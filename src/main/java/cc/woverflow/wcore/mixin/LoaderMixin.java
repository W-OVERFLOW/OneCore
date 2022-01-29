package cc.woverflow.wcore.mixin;

import cc.woverflow.wcore.WCore;
import net.minecraftforge.fml.common.Loader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Loader.class, remap = false, priority = Integer.MIN_VALUE)
public class LoaderMixin {

    @Inject(method = "initializeMods", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/LoadController;distributeStateMessage(Lnet/minecraftforge/fml/common/LoaderState;[Ljava/lang/Object;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void onInit(CallbackInfo ci) {
        WCore.INSTANCE.onInit(null);
    }

    @Inject(method = "initializeMods", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/fml/common/LoadController;distributeStateMessage(Lnet/minecraftforge/fml/common/LoaderState;[Ljava/lang/Object;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void onPostInit(CallbackInfo ci) {
        WCore.INSTANCE.onPostInit(null);
    }
}
