package dev.aika.clienttweaks.event;

import dev.aika.clienttweaks.feature.Unfocused;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

@UtilityClass
@SuppressWarnings("unused")
public class ClientEvents {
    @Getter
    private static boolean initialized = false;

    public void onClientStarted() {
        initialized = true;
    }

    public void onClientStarted(Minecraft ignoredMinecraft) {
        onClientStarted();
    }

    public void onWindowFocusChanged(boolean focused) {
        Unfocused.onWindowFocusChanged(focused);
    }
}