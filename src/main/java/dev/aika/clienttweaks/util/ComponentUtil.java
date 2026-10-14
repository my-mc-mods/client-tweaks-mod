package dev.aika.clienttweaks.util;

import lombok.experimental.UtilityClass;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;

@UtilityClass
public class ComponentUtil {
    public Component translatableWithFallbackKey(
            String key,
            String fallbackKey,
            Object... args
    ) {
        Language language = Language.getInstance();
        if (language.has(key)) return Component.translatable(key, args);
        if (language.has(fallbackKey)) return Component.translatable(fallbackKey, args);
        return Component.literal(key);
    }
}