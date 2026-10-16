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
    private transient final Boolean isDevEnv = ModLoaderAccess.INSTANCE.isDevelopmentEnvironment();

    private Boolean removeRealmsButton = isDevEnv;
    private Boolean betterLanguageFilter = true;
    private Boolean smartFireOverlay = false;
    private Boolean hideUnsecureWarningToast = isDevEnv;

    private UnfocusedConfig unfocused = new UnfocusedConfig();
    private Set<String> disabledMixins = new LinkedHashSet<>();

    @Getter @Setter
    @Accessors(fluent = true)
    public static class UnfocusedConfig {
        private int framerateLimit = 10;
        private Boolean mute = true;
    }
}