package dev.aika.clienttweaks.compat;

//? if fabric {
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.aika.clienttweaks.boot.ModConfig;

public class ModMenuIntegration implements ModMenuApi {
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> {
            ModConfig.load();
            return parent;
        };
    }
}
//?}