package cc.woverflow.onecore.internal.plugin

import org.objectweb.asm.tree.ClassNode
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin
import org.spongepowered.asm.mixin.extensibility.IMixinInfo

class OneCorePlugin : IMixinConfigPlugin {
    override fun onLoad(mixinPackage: String?) {

    }

    override fun getRefMapperConfig(): String? {
        return null
    }

    override fun shouldApplyMixin(targetClassName: String?, mixinClassName: String?): Boolean {
        return true
    }

    override fun acceptTargets(myTargets: MutableSet<String>?, otherTargets: MutableSet<String>?) {

    }

    @Suppress("UnnecessaryVariable")
    override fun getMixins(): MutableList<String> {
        val array = arrayListOf("")
        //#if MC>=11800
        //$$ array.add("MinecraftAccessor")
        //#endif
        //#if FABRIC==1
        //$$ array.add("ClickEventMixin")
        //$$ array.add("ScreenMixin")
        //#endif
        return array
    }

    override fun preApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {

    }

    override fun postApply(
        targetClassName: String?,
        targetClass: ClassNode?,
        mixinClassName: String?,
        mixinInfo: IMixinInfo?
    ) {

    }
}