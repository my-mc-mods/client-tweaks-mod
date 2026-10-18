package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.event.ClientEvents;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;

@UtilityClass
public class Unfocused {
    @Getter
    private static boolean muted;

    public void onWindowFocusChanged(boolean focused) {
        if (!ClientEvents.isInitialized()) return;

        final var conf = ModConfig.get().unfocused();
        final var mc = Minecraft.getInstance();

        if (conf.framerateLimit() > 0) {
            setFramerateLimit(focused ? mc.options.framerateLimit().get() : conf.framerateLimit());
        }
        if (conf.mute()) setMuted(!focused);
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
        Minecraft.getInstance().getSoundManager().refreshCategoryVolume(SoundSource.MASTER); //?} else {
        /*var soundEngine = (dev.aika.clienttweaks.mixin.Accessor.SoundEngineAccessor)
                Minecraft.getInstance().getSoundManager().soundEngine;
        if (!soundEngine.isLoaded()) return;

        soundEngine.getInstanceToChannel().forEach((instance, handle) -> {
            if (instance.getSource() == SoundSource.MASTER) {
                float volume = soundEngine.calculateVolume(instance);
                handle.execute((channel) -> channel.setVolume(volume));
            }
        });*///?}
    }

    private void setFramerateLimit(int limit) {
        Minecraft.getInstance()./*? if >1.21.1 {*/getFramerateLimitTracker()/*?} else {*//*getWindow()*//*?}*/
                .setFramerateLimit(limit);
    }
}