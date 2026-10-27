package dev.aika.clienttweaks.mixin.ClothConfigFix;

//? if cloth_config && >=1.21.11 {
import me.shedaniel.clothconfig2.gui.entries.MultiElementListEntry;
import me.shedaniel.math.Rectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = MultiElementListEntry.CategoryLabelWidget.class, remap = false)
public abstract class MultiElementListEntry_CategoryLabelWidgetMixin implements GuiEventListener, NarratableEntry {
    @Shadow(remap = false) @Final private Rectangle rectangle;

    @Override public boolean /* FocusedFix */ isMouseOver(double mouseX, double mouseY) {
        return this.rectangle.contains(mouseX, mouseY);
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public abstract class MultiElementListEntry_CategoryLabelWidgetMixin {
}
*///?}