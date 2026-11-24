package dev.aika.clienttweaks.boot;

import dev.aika.clienttweaks.Constants;
import dev.aika.clienttweaks.config.ClientTweaksConfig;
import dev.aika.clienttweaks.config.ConfigManager;
import lombok.Getter;
import lombok.experimental.UtilityClass;

import java.nio.file.Path;
import java.util.LinkedHashSet;

@SuppressWarnings("unused")
@UtilityClass
public class ModConfig {
    @Getter(lazy = true)
    private static final ConfigManager<ClientTweaksConfig> config = ConfigManager
            .create(ClientTweaksConfig.class, Constants.MOD_ID)
            .load();

    public ClientTweaksConfig get() {
        return getConfig().get();
    }

    public void save() {
        getConfig().save();
    }

    public void load() {
        getConfig().load();
        final var settings = get();
        if (settings.disabledMixins() == null) settings.disabledMixins(new LinkedHashSet<>());
        // Preserve switches saved before the accessor moved into the Unfocused group.
        if (settings.disabledMixins().remove("dev.aika.clienttweaks.mixin.Accessor.SoundEngineAccessor")) {
            settings.disabledMixins().add("dev.aika.clienttweaks.mixin.Unfocused.SoundEngineAccessor");
        }
    }

    public Path getConfigDirectory() {
        return getConfig().getConfigDirectory();
    }
}
