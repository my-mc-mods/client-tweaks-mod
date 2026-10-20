package dev.aika.clienttweaks;

//? if neoforge {
/*import dev.aika.clienttweaks.boot.ModConfig;
import dev.aika.clienttweaks.event.ClientEvents;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = Constants.MOD_ID, dist = Dist.CLIENT)
public final class ClientTweaksModNeoForge {
    public ClientTweaksModNeoForge(ModContainer container) {
        ClientTweaksMod.init();
        container.registerExtensionPoint(IConfigScreenFactory.class,
                (ignoreContainer, parent) -> {
                    ModConfig.load();
                    return parent;
                }
        );
    }

    @EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
    public static class Events {
        //? if <1.21.11
        //private static boolean initialized = false;

        //? if >=1.21.6 {
        @SubscribeEvent
        public static void onClientStarted(net.neoforged.neoforge.client.event.lifecycle.ClientStartedEvent event) {
            ClientEvents.onClientStarted();
        } //?}

        @SubscribeEvent
        public static void onClientTick(ClientTickEvent.Post event) {
            //? if <1.21.11 {
            /^if (initialized) return;
            initialized = true;
            ClientEvents.onClientStarted(); ^///?}
            ClientEvents.onEndClientTick(Minecraft.getInstance());
        }
    }
}
*///?}