package dev.aika.clienttweaks.feature;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.event.ClientEvents;
import dev.aika.clienttweaks.mixin.Accessor.SoundEngineAccessor;
import dev.aika.clienttweaks.mixin.Accessor.SoundManagerAccessor;
import lombok.experimental.UtilityClass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;

@UtilityClass
public class Unfocused {
    private static SoundEngine soundEngine;

    public void init() {
        soundEngine = ((SoundManagerAccessor) Minecraft.getInstance().getSoundManager()).clienttweaks$getSoundEngine();
    }

    public void onWindowFocusChanged(boolean focused) {
        if (!ClientEvents.isInitialized()) return;

        final var conf = ModConfig.get().unfocused();
        final var mc = Minecraft.getInstance();

        if (conf.framerateLimit() > 0) {
            setFramerateLimit(focused ? mc.options.framerateLimit().get() : conf.framerateLimit());
        }
        if (conf.mute()) {
            if (!focused) setVolume(0f);
            else refreshMasterVolume();
        }
    }

    @SuppressWarnings({"SameParameterValue", "unused"})
    private void setVolume(float volume) {
        if (!((SoundEngineAccessor) soundEngine).clienttweaks$isLoaded()) return;
        ((SoundEngineAccessor) soundEngine).clienttweaks$getInstanceToChannel()
                .forEach((instance, handle)
                        -> handle.execute((channel) -> channel.setVolume(volume)));
    }

    @SuppressWarnings("SameParameterValue")
    private void refreshMasterVolume() {
        //? if >=1.21.11 {
        Minecraft.getInstance().getSoundManager().refreshCategoryVolume(SoundSource.MASTER); //?} else {
        /*if (!((SoundEngineAccessor) soundEngine).clienttweaks$isLoaded()) return;

        ((SoundEngineAccessor) soundEngine).clienttweaks$getInstanceToChannel().forEach((instance, handle) -> {
            if (instance.getSource() == SoundSource.MASTER) {
                float volume = ((SoundEngineAccessor) soundEngine).clienttweaks$calculateVolume(instance);
                handle.execute((channel) -> channel.setVolume(volume));
            }
        });*///?}
    }

    private void setFramerateLimit(int limit) {
        Minecraft.getInstance()./*? if >1.21.1 {*/getFramerateLimitTracker()/*?} else {*//*getWindow()*//*?}*/
                .setFramerateLimit(limit);
    }
}