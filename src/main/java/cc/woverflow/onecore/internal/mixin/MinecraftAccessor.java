//#if MC>=11800
//$$ package cc.woverflow.onecore.internal.mixin;

//$$ import com.mojang.authlib.minecraft.UserApiService;
//$$ import org.spongepowered.asm.mixin.Mixin;
//$$ import org.spongepowered.asm.mixin.gen.Accessor;

    //#if FABRIC==1
    //$$ @Mixin(net.minecraft.client.MinecraftClient.class)
    //#else
    //$$ @Mixin(net.minecraft.client.Minecraft.class)
    //#endif
//$$ public interface MinecraftAccessor {
//$$     @Accessor
//$$     UserApiService getUserApiService();
//$$ }
//#endif