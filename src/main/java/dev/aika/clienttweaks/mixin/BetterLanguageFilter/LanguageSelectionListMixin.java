package dev.aika.clienttweaks.mixin.BetterLanguageFilter;

//? if >=1.21.11 {
import org.spongepowered.asm.mixin.Mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.aika.clienttweaks.boot.ModConfig;
import net.minecraft.client.resources.language.LanguageInfo;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

@Mixin(net.minecraft.client.gui.screens.options.LanguageSelectScreen.LanguageSelectionList.class)
public abstract class LanguageSelectionListMixin {
    @WrapOperation(method = "filterEntries",
            at = @At(value = "INVOKE", target = "Ljava/util/stream/Stream;filter(Ljava/util/function/Predicate;)Ljava/util/stream/Stream;"))
    private Stream<Map.Entry<String, LanguageInfo>> modifyFilter(
            Stream<Map.Entry<String, LanguageInfo>> instance,
            Predicate<Map.Entry<String, LanguageInfo>> original,
            Operation<Stream<Map.Entry<String, LanguageInfo>>> operation,
            String filter
    ) {
        if (!ModConfig.get().betterLanguageFilter()) return operation.call(instance, original);

        final var f = filter.toLowerCase(Locale.ROOT);
        return operation.call(instance, (Predicate<Map.Entry<String, LanguageInfo>>)
                entry -> filter.isEmpty()
                        || entry.getValue().name().toLowerCase(Locale.ROOT).contains(f)
                        || entry.getValue().region().toLowerCase(Locale.ROOT).contains(f)
                        || entry.getKey().toLowerCase(Locale.ROOT).contains(f)
                        || entry.getKey().replace("_", "").toLowerCase(Locale.ROOT).contains(f));
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public abstract class LanguageSelectionListMixin {
}
*///?}