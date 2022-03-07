package cc.woverflow.onecore.init;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unused")
public class OneCoreInit {
    public static void initialize() {
        if ("true".equals(System.getProperty("essential.loader.relaunched"))) {
            MixinBootstrap.init();
            Mixins.addConfiguration("mixins.onecore.json");
            String devMixin = System.getProperty("onecore.mixin");
            if (devMixin != null && !devMixin.isEmpty()) {
                Mixins.addConfiguration(System.getProperty("onecore.mixin"));
            }
        }
        System.out.println("OneCore has been initialized :D");
    }
}
