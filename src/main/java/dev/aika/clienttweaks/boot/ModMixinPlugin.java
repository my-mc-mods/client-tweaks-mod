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

@SuppressWarnings({"RedundantMethodOverride", "SameParameterValue", "unused"})
public class ModMixinPlugin implements IMixinConfigPlugin {
    public static Set<String> disabledMixins = Sets.newHashSet();
    public static Set<String> mixins = Sets.newHashSet();

    @Override
    public void onLoad(String mixinPackage) {
        if (!ModLoaderAccess.INSTANCE.isClient()) {
            Constants.LOG.info("Oops! This is a client-side mod. Nothing to do here!");
            return;
        }
        ModConfig.load();

        //? if <1.21.11
        //disableMixin("dev.aika.clienttweaks.mixin.BetterLanguageFilter.LanguageSelectionListMixin");

        disableUnless(ModConfig.get()::removeRealmsButton,
                "dev.aika.clienttweaks.mixin.RemoveRealmsButton.TitleScreenMixin");

        disableUnless(ModLoaderAccess.INSTANCE::isClothConfigLoaded,
                //? if >=26 {
                "dev.aika.clienttweaks.mixin.ClothConfigFix.DynamicElementListWidget_ElementEntryMixin",
                "dev.aika.clienttweaks.mixin.ClothConfigFix.MultiElementListEntry_CategoryLabelWidgetMixin", //?}
                "dev.aika.clienttweaks.mixin.ClothConfigFix.TooltipListEntryMixin");
    }

    private void disableIf(BooleanSupplier condition, @Language(value = "JAVA", suffix = ".class") String... mixinClassNames) {
        for (@Language(value = "JAVA", suffix = ".class") String mixinClassName : mixinClassNames) {
            if (condition.getAsBoolean()) disableMixin(mixinClassName);
        }
    }

    private void disableUnless(BooleanSupplier condition, @Language(value = "JAVA", suffix = ".class") String... mixinClassNames) {
        for (@Language(value = "JAVA", suffix = ".class") String mixinClassName : mixinClassNames) {
            if (!condition.getAsBoolean()) disableMixin(mixinClassName);
        }
    }

    private void disableMixin(@Language(value = "JAVA", suffix = ".class") String mixinClassName) {
        disabledMixins.add(mixinClassName);
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        mixins.add(mixinClassName);
        return !disabledMixins.contains(mixinClassName) && !ModConfig.get().disabledMixins().contains(mixinClassName);
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