package dev.aika.clienttweaks.mixin.PingFpsHud;

import dev.aika.clienttweaks.feature.PingFpsHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(/*? if >=26.2 {*//*net.minecraft.client.gui.Hud.class*//*?} else {*/net.minecraft.client.gui.Gui.class/*?}*/)
public abstract class HudMixin {
    @Inject(method = /*? if >=26 {*/"extractRenderState"/*?} else {*//*"render"*//*?}*/, at = @At("TAIL"))
    private void renderPingFpsHud(net.minecraft.client.gui.GuiGraphicsExtractor graphics,
                                  //? if <=26.3
                                  net.minecraft.client.DeltaTracker deltaTracker,
                                  CallbackInfo ci) {
        PingFpsHud.render(graphics, false);
    }
}
