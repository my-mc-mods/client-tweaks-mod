package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.event.ClientEvents;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;

@UtilityClass
public class Unfocused {
    @Getter
    private static boolean muted;

    public void onWindowFocusChanged(boolean focused) {
        if (!ClientEvents.isInitialized()) return;

        final var mc = Minecraft.getInstance();

        final int unfocusedFpsLimit = ModConfig.get().unfocusedFpsLimit();
        if (unfocusedFpsLimit > 0) {
            setFramerateLimit(focused ? mc.options.framerateLimit().get() : unfocusedFpsLimit);
        }
        if (ModConfig.get().unfocusedMute()) setMuted(!focused);
    }

    private void setMuted(boolean value) {
        if (value != muted) {
            muted = value;
            refreshMasterVolume();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void refreshMasterVolume() {
        //? if >=1.21.11 {
        Minecraft.getInstance().getSoundManager().refreshCategoryVolume(net.minecraft.sounds.SoundSource.MASTER); //?} else {
        /*var soundEngine = (dev.aika.clienttweaks.mixin.Accessor.SoundEngineAccessor)
                Minecraft.getInstance().getSoundManager().soundEngine;
        if (!soundEngine.clienttweaks$isLoaded()) return;

        soundEngine.clienttweaks$getInstanceToChannel().forEach((instance, handle) -> {
            float volume = muted ? 0.0f : soundEngine.clienttweaks$calculateVolume(instance);
            handle.execute((channel) -> channel.setVolume(volume));
        });*///?}
    }

    private void setFramerateLimit(int limit) {
        Minecraft.getInstance()./*? if >1.21.1 {*/getFramerateLimitTracker()/*?} else {*//*getWindow()*//*?}*/
                .setFramerateLimit(limit);
    }
}