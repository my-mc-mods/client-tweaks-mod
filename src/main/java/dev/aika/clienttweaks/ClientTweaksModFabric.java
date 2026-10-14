package dev.aika.clienttweaks;

//? if fabric {
import dev.aika.clienttweaks.event.ClientEvents;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public final class ClientTweaksModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        ClientTweaksMod.init();
        ClientLifecycleEvents.CLIENT_STARTED.register(ClientEvents::onClientStarted);
    }
}
//?}