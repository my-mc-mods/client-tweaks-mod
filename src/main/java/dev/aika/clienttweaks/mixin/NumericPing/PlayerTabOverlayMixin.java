package dev.aika.clienttweaks.mixin.NumericPing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.feature.NumericPing;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
    @Shadow @Final private Minecraft minecraft;

    @ModifyExpressionValue(
            method = /*? if >26.3 {*//*"extractContents"*//*?} elif >26 {*/"extractRenderState"/*?} else {*//*"render"*//*?}*/,
            at = @At(value = "CONSTANT", args = "intValue=13"))
    private int modifySlotWidth(int original) {
        if (!ModConfig.get().numericPing().enabled()) return original;
        return original + ModConfig.get().numericPing().slotPaddingLeft();
    }

    //? if >=26 {
    @Inject(method = "extractPingIcon", at = @At("HEAD"), cancellable = true)
    private void renderPingIcon(net.minecraft.client.gui.GuiGraphicsExtractor graphics, //?} else {
    /*@Inject(method = "renderPingIcon", at = @At("HEAD"), cancellable = true)
    private void renderPingIcon(net.minecraft.client.gui.GuiGraphicsExtractor graphics, *///?}
                                int slotWidth, int xo, int yo, PlayerInfo info, CallbackInfo ci) {
        if (!ModConfig.get().numericPing().enabled()) return;
        NumericPing.render(this.minecraft, graphics, slotWidth, xo, yo, info);
        if (!ModConfig.get().numericPing().showPingIcon()) ci.cancel();
    }
}