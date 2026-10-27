package dev.aika.clienttweaks.compat;

import dev.aika.clienttweaks.client.gui.screen.ClothConfigWarning;
import dev.aika.clienttweaks.util.ModLoaderAccess;
import net.minecraft.client.gui.screens.Screen;

public final class ClothConfigIntegration {
    public static Screen getConfigScreen(Screen parent) {
        //? if cloth_config {
        if (ModLoaderAccess.INSTANCE.isClothConfigLoaded())
            return dev.aika.clienttweaks.client.gui.screen.ModClothConfigScreen.create(parent);
        else return new ClothConfigWarning(parent);
        //?} else
        //return new ClothConfigWarning(parent);
    }
}