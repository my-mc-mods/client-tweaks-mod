package dev.aika.clienttweaks.boot;

import dev.aika.clienttweaks.Constants;
import dev.aika.clienttweaks.config.ClientTweaksConfig;
import dev.aika.clienttweaks.config.ConfigManager;
import lombok.Getter;
import lombok.experimental.UtilityClass;

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
    }
}