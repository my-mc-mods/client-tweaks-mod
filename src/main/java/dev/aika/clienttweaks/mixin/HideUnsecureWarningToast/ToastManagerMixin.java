package dev.aika.clienttweaks.mixin.HideUnsecureWarningToast;

import dev.aika.clienttweaks.boot.ModConfig;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.components.toasts.Toast;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

//? if >1.21.1 {
@Mixin(net.minecraft.client.gui.components.toasts.ToastManager.class) //?} else
//@Mixin(net.minecraft.client.gui.components.toasts.ToastComponent.class)
public abstract class ToastManagerMixin {
    @Inject(method = "addToast", at = @At("HEAD"), cancellable = true)
    public void addToast(Toast toast, CallbackInfo ci) {
        if (!ModConfig.get().hideUnsecureWarningToast()) return;
        if (toast instanceof SystemToast st
                && st.getToken() == SystemToast.SystemToastId.UNSECURE_SERVER_WARNING) {
            ci.cancel();
        }
    }
}