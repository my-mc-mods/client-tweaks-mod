package dev.aika.clienttweaks.mixin.SmartFireOverlay;

import dev.aika.clienttweaks.boot.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.world.effect.MobEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public abstract class ScreenEffectRendererMixin {
    //? if >=26.2 {
    /*@Inject(method = "submitFire", cancellable = true, at = @At("HEAD"))
    private static void submitFire(com.mojang.blaze3d.vertex.PoseStack poseStack,
                                   net.minecraft.client.renderer.SubmitNodeCollector submitNodeCollector,
                                   net.minecraft.client.renderer.texture.TextureAtlasSprite sprite,
                                   CallbackInfo ci) { *///?} elif >1.21.1 <26.2 {
    @Inject(method = "renderFire", cancellable = true, at = @At("HEAD"))
    private static void renderScreenEffect(CallbackInfo ci) { //?} else {
    /*@Inject(method = "renderFire", cancellable = true, at = @At("HEAD"))
    private static void renderScreenEffect(CallbackInfo ci) { *///?}
        if (!ModConfig.get().smartFireOverlay()) return;
        final var player = Minecraft.getInstance().player;
        if (player == null || player.isSpectator()) return;
        if (player.isCreative() || player.hasEffect(MobEffects.FIRE_RESISTANCE)) ci.cancel();
    }
}