package dev.aika.clienttweaks.mixin.ClothConfigFix;

//? if cloth_config && >=1.21.11 {
import me.shedaniel.clothconfig2.gui.widget.DynamicElementListWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jspecify.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = DynamicElementListWidget.ElementEntry.class, remap = false)
public abstract class DynamicElementListWidget_ElementEntryMixin {
    @Shadow private @Nullable GuiEventListener focused;

    @Inject(method = "setFocused", at = @At("HEAD"), cancellable = true)
    private void focusedFix(GuiEventListener guiEventListener, CallbackInfo ci) {
        if (this.focused == guiEventListener) ci.cancel();
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public abstract class DynamicElementListWidget_ElementEntryMixin {
}
*///?}