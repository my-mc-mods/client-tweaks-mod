package dev.aika.clienttweaks.boot;

import com.google.common.collect.ImmutableSortedSet;
import dev.aika.clienttweaks.Constants;
import dev.aika.clienttweaks.util.ModLoaderAccess;
import lombok.Getter;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.IOException;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SuppressWarnings("RedundantMethodOverride")
public class ModMixinPlugin implements IMixinConfigPlugin {
    @Getter private static ImmutableSortedSet<String> availableMixins = ImmutableSortedSet.of();
    private final Set<String> detectedMixins = new HashSet<>();
    private String mixinPrefix;

    @Override
    public void onLoad(String mixinPackage) {
        mixinPrefix = mixinPackage + ".";
        if (!ModLoaderAccess.INSTANCE.isClient()) {
            Constants.LOG.info("Oops! This is a client-side mod. Nothing to do here!");
            return;
        }
        ModConfig.load();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isAvailable(mixinClassName)) return false;
        detectedMixins.add(mixinClassName);
        if (ModConfig.get().disabledMixins().contains(mixinClassName)) {
            Constants.LOG.warn("Mixin {} is disabled in the config and will not be applied to {}", mixinClassName, targetClassName);
            return false;
        }
        return true;
    }

    private boolean isAvailable(String mixinClassName) {
        if (!ModLoaderAccess.INSTANCE.isClient()) return false;

        final String relativeName = mixinClassName.substring(mixinPrefix.length());
        if (relativeName.equals("RemoveRealmsButton.TitleScreenMixin")
            && !ModConfig.get().removeRealmsButton()) return false;
        if (relativeName.startsWith("ClothConfigFix.") && !ModLoaderAccess.INSTANCE.isClothConfigLoaded()) return false;
        //? if <1.21.11 {
        /*return !relativeName.startsWith("LanguageCodeFilter.")
                && (!relativeName.startsWith("ClothConfigFix.")
                    || relativeName.equals("ClothConfigFix.TooltipListEntryMixin"));
        *///?} else {
        return !relativeName.equals("Unfocused.SoundEngineAccessor");
        //?}
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        if (!ModLoaderAccess.INSTANCE.isClient()) return;
        availableMixins = ImmutableSortedSet.copyOf(detectedMixins);
        writeAvailableMixins();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
    }

    private void writeAvailableMixins() {
        final var path = ModConfig.getConfigDirectory().resolve("available_mixins.txt");
        try {
            Files.createDirectories(path.getParent());
            Files.write(path, availableMixins);
        } catch (IOException e) {
            Constants.LOG.error("Failed to write available mixins list: {}", path, e);
        }
    }
}
