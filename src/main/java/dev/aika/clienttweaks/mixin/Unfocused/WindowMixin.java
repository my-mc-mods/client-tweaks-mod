package dev.aika.clienttweaks.mixin.Unfocused;

import com.mojang.blaze3d.platform.Window;
import dev.aika.clienttweaks.event.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Window.class)
public abstract class WindowMixin {
    @Inject(method = "onFocus", at = @At("RETURN"))
    private void onWindowFocusChanged(
            //? if <26.3
            long handle,
            boolean focused, CallbackInfo ci
    ) {
        ClientEvents.onWindowFocusChanged(focused);
    }
}