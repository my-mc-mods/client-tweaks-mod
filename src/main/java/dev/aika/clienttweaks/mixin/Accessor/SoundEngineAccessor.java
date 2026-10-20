package dev.aika.clienttweaks.mixin.Accessor;

import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Map;

@SuppressWarnings("unused")
@Mixin(SoundEngine.class)
public interface SoundEngineAccessor {
    @Accessor("instanceToChannel")
    Map<SoundInstance, ChannelAccess.ChannelHandle> clienttweaks$getInstanceToChannel();

    @Accessor("loaded")
    boolean clienttweaks$isLoaded();

    @SuppressWarnings("unused")
    @Invoker("calculateVolume")
    float clienttweaks$calculateVolume(SoundInstance instance);
}