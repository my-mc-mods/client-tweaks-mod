package dev.aika.clienttweaks;

//? if fabric {
import dev.aika.clienttweaks.event.ClientEvents;
import dev.aika.clienttweaks.feature.StatusIconsHud;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

public final class ClientTweaksModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClientTweaksMod.init();

        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::onClientStarted);
        ClientTickEvents.END_CLIENT_TICK.register(ClientEvents::onEndClientTick);

        //? if >26.3 {
        /*net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(
                net.minecraft.resources.Identifier.fromNamespaceAndPath("gizmoclienttweaks", "status_icons"),
                graphics -> StatusIconsHud.render(graphics, 0.0f));
        *///?} elif >=1.21.11 {
        net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry.addLast(
                net.minecraft.resources.Identifier.fromNamespaceAndPath("gizmoclienttweaks", "status_icons"),
                (graphics, deltaTracker) ->
                        StatusIconsHud.render(graphics, deltaTracker.getGameTimeDeltaPartialTick(false)));
        //?} else {
        /*net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback.EVENT
                .register((graphics, deltaTracker) ->
                        StatusIconsHud.render(graphics, deltaTracker.getGameTimeDeltaPartialTick(false)));
        *///?}
    }
}
//?}