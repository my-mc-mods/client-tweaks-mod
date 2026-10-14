package dev.aika.clienttweaks.boot;

import com.google.common.collect.*;
import dev.aika.clienttweaks.Constants;
import dev.aika.clienttweaks.util.ModLoaderAccess;
import org.intellij.lang.annotations.Language;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.function.BooleanSupplier;

@SuppressWarnings("RedundantMethodOverride")
public class ModMixinPlugin implements IMixinConfigPlugin {
    Set<String> disabledMixins;

    @Override
    public void onLoad(String mixinPackage) {
        if (!ModLoaderAccess.INSTANCE.isClient()) {
            Constants.LOG.info("Oops! This is a client-side mod. Nothing to do here!");
            return;
        }
        ModConfig.load();

        disabledMixins = ModConfig.get().disabledMixins();

        //? if <1.21.11
        //disabledMixins.add("dev.aika.clienttweaks.mixin.BetterLanguageFilter.LanguageSelectionListMixin");

        disableMixinIf("dev.aika.clienttweaks.mixin.RemoveRealmsButton.TitleScreenMixin",
                () -> !ModConfig.get().removeRealmsButton());
    }

    @SuppressWarnings("SameParameterValue")
    private void disableMixinIf(@Language(value = "JAVA", suffix = ".class") String mixinClassName,
                                BooleanSupplier condition) {
        if (condition.getAsBoolean()) disabledMixins.add(mixinClassName);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!disabledMixins.contains(mixinClassName)) return true;
        Constants.LOG.warn("Failed to apply mixin class {}", mixinClassName);
        return false;
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public List<String> getMixins() {
        return Collections.emptyList();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }
}