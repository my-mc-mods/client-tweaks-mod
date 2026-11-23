package dev.aika.clienttweaks.mixin.ClientPingRefresh;

import com.mojang.authlib.GameProfile;
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
        final var listener = Minecraft.getInstance().getConnection();
        if (listener == null || !listener.getLocalGameProfile()./*? if >=1.21.9 {*/id()/*?} else {*//*getId()*//*?}*/
                .equals(this.getProfile()./*? if >=1.21.9 {*/id()/*?} else {*//*getId()*//*?}*/)) return;
        final int latency = ClientPingRefresh.getLatencyMs(listener);
        // Keep the server's player-list latency available until an active sample arrives.
        if (latency >= 0) cir.setReturnValue(latency);
    }
}
