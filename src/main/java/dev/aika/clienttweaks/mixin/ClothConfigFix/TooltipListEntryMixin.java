package dev.aika.clienttweaks.mixin.ClothConfigFix;

import org.spongepowered.asm.mixin.Mixin;

//? if cloth_config {
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
//?}

//? if !cloth_config {
/*@Mixin(value = net.minecraft.world.entity.vehicle.minecart.Minecart.class)
public abstract class TooltipListEntryMixin { *///?} else {
@Mixin(value = me.shedaniel.clothconfig2.gui.entries.TooltipListEntry.class, remap = false)
public abstract class TooltipListEntryMixin { //?}
    //? if cloth_config {
    @ModifyArg(method = /*? if >26 {*/"lambda$extractRenderState$0"/*?} else {*//*"lambda$render$0"*//*?}*/,
            at = @At(value = "INVOKE", target = "Lme/shedaniel/clothconfig2/api/Tooltip;of(Lme/shedaniel/math/Point;[Lnet/minecraft/util/FormattedCharSequence;)Lme/shedaniel/clothconfig2/api/Tooltip;"))
    private me.shedaniel.math.Point tooltipFix(me.shedaniel.math.Point location) {
        return new me.shedaniel.math.Point(location.getX(), location.getY() + 26);
    } //?}
}