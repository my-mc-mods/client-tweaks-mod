package dev.aika.clienttweaks.mixin.ClothConfigFix;

//? if cloth_config && >=1.21.11 {
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = MultiElementListEntry.class, remap = false)
public abstract class MultiElementListEntryMixin {
    @Shadow(remap = false) @Final private MultiElementListEntry<?>.CategoryLabelWidget widget;

    @Inject(method = "isMouseOver", at = @At("HEAD"), cancellable = true, remap = false)
    private void checkWidgetMouseOver(double mouseX, double mouseY, CallbackInfoReturnable<Boolean> cir) {
        // Check if the mouse is over the widget's rectangle (including the -/+ icon area)
        if (this.widget.isMouseOver(mouseX, mouseY)) {
            cir.setReturnValue(true);
        }
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public abstract class MultiElementListEntryMixin {
}
*///?}