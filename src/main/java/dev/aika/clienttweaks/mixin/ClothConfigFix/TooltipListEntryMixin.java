package dev.aika.clienttweaks.mixin.ClothConfigFix;

//? if cloth_config {
import me.shedaniel.math.Point;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = me.shedaniel.clothconfig2.gui.entries.TooltipListEntry.class, remap = false)
public abstract class TooltipListEntryMixin {
    @ModifyArg(method = /*? if >26 {*/"lambda$extractRenderState$0"/*?} else {*//*"lambda$render$0"*//*?}*/,
            at = @At(value = "INVOKE", target = "Lme/shedaniel/clothconfig2/api/Tooltip;of(Lme/shedaniel/math/Point;[Lnet/minecraft/util/FormattedCharSequence;)Lme/shedaniel/clothconfig2/api/Tooltip;"))
    private Point tooltipFix(Point location) {
        return new Point(location.getX(), location.getY() + 26);
    }
}
//?} else {
/*@org.spongepowered.asm.mixin.Mixin(net.minecraft.client.Minecraft.class)
public abstract class TooltipListEntryMixin {
}
*///?}