package dev.aika.clienttweaks.mixin.ClientPingRefresh;

import com.mojang.authlib.GameProfile;
import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.feature.ClientPingRefresh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerInfo.class)
public abstract class PlayerInfoMixin {
    @Shadow public abstract GameProfile getProfile();

    @Inject(method = "getLatency", at = @At("HEAD"), cancellable = true)
    private void modifyLatency(CallbackInfoReturnable<Integer> cir) {
        final int interval = ModConfig.get().clientPingRefresh();
        if (interval < 1 || interval >= 30) return;
        if (Minecraft.getInstance()
                .getGameProfile()./*? if >=1.21.9 {*/name()/*?} else {*//*getName()*//*?}*/
                .equals(this.getProfile()./*? if >=1.21.9 {*/name()/*?} else {*//*getName()*//*?}*/))
            cir.setReturnValue((int) ClientPingRefresh.getLatencyMs());
    }
}