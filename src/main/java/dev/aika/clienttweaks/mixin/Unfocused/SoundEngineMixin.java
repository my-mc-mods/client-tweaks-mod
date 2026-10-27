package dev.aika.clienttweaks.mixin.Unfocused;

import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.feature.Unfocused;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SoundEngine.class)
public abstract class SoundEngineMixin {
    @Inject(method = "calculateVolume(FLnet/minecraft/sounds/SoundSource;)F", at = @At("HEAD"), cancellable = true)
    public void calculateVolume(float volume, SoundSource source, CallbackInfoReturnable<Float> cir) {
        if (!ModConfig.get().unfocusedMute() || !Unfocused.isMuted()) return;
        cir.setReturnValue(0.0f);
    }
}