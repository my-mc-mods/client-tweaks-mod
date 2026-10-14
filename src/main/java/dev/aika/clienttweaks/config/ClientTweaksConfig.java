package dev.aika.clienttweaks.config;

import dev.aika.clienttweaks.util.ModLoaderAccess;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter @Setter
@Accessors(fluent = true)
public class ClientTweaksConfig {
    private Boolean removeRealmsButton = // 开发环境默认启用
            ModLoaderAccess.INSTANCE.isDevelopmentEnvironment();
    private Boolean betterLanguageFilter = true;
    private Set<String> disabledMixins = new LinkedHashSet<>();
}