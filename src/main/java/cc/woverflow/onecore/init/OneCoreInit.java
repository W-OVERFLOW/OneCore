package cc.woverflow.onecore.init;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.Mixins;

@SuppressWarnings("unused")
public class OneCoreInit {
    /**
     * Starts the main OneCore process and initalizes mixins.
     */
    public static void initialize() {
        if (
                //#if MODERN==0
                "true".equals(System.getProperty("essential.loader.relaunched"))
                //#else
                //$$ false
                //#endif
        ) {
            MixinBootstrap.init();
            Mixins.addConfiguration("mixins.onecore.json");
            String devMixin = System.getProperty("onecore.mixin");
            if (devMixin != null && !devMixin.isEmpty()) {
                Mixins.addConfiguration(System.getProperty("onecore.mixin"));
            }
        }
        System.setProperty("onecore.loader.init", "true");
        System.out.println("OneCore has been initialized :D");
    }
}
