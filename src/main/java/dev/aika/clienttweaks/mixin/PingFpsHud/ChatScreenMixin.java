package dev.aika.clienttweaks.mixin.PingFpsHud;

import dev.aika.clienttweaks.feature.PingFpsHud;
import net.minecraft.client.gui.screens.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin {
    // ChatScreen's parameter names vary across mappings; injection only uses their types.
    @SuppressWarnings("NameDoesntMatchTargetClass")
    @Inject(method = /*? if >=26 {*/"extractRenderState"/*?} else {*//*"render"*//*?}*/, at = @At("TAIL"))
    private void renderPingFpsHud(net.minecraft.client.gui.GuiGraphicsExtractor graphics,
                                  //? if <=26.3
                                  int mouseX, int mouseY, float delta,
                                  CallbackInfo ci) {
        PingFpsHud.render(graphics, true);
    }
}
