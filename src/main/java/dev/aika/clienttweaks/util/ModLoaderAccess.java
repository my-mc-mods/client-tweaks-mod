package dev.aika.clienttweaks.util;

import org.jspecify.annotations.NonNull;

import java.nio.file.Path;

@SuppressWarnings({"unused", "CommentedOutCode"})
public sealed interface ModLoaderAccess {
    boolean isModLoaded(String id);

    boolean isDevelopmentEnvironment();

    @NonNull Path getConfigDir();

    boolean isClient();

    boolean isServer();

    default String getMinecraftVersion() {
        return net.minecraft.DetectedVersion.tryDetectVersion().name();
    }

    default boolean isClothConfigLoaded() {
        return INSTANCE.isModLoaded(/*? if fabric {*/"cloth-config2"/*?} else {*//*"cloth_config"*//*?}*/);
    }

    ModLoaderAccess INSTANCE = /*? if fabric {*/new FabricLoaderAccess();/*?} elif neoforge *///new NeoForgeLoaderAccess();

    //? if fabric {
    final class FabricLoaderAccess implements ModLoaderAccess {
        private final net.fabricmc.loader.api.FabricLoader loader = net.fabricmc.loader.api.FabricLoader.getInstance();
        private final net.fabricmc.api.EnvType envType = loader.getEnvironmentType();

        @Override public boolean isModLoaded(String id) {
            return loader.isModLoaded(id);
        }

        @Override public boolean isDevelopmentEnvironment() {
            return loader.isDevelopmentEnvironment();
        }

        @Override public @NonNull Path getConfigDir() {
            return loader.getConfigDir();
        }

        @Override public boolean isClient() {
            return envType == net.fabricmc.api.EnvType.CLIENT;
        }

        @Override public boolean isServer() {
            return envType == net.fabricmc.api.EnvType.SERVER;
        }
    }
    //?} elif neoforge {
    /*final class NeoForgeLoaderAccess implements ModLoaderAccess {
        private final net.neoforged.api.distmarker.Dist dist = net.neoforged.fml.loading.FMLEnvironment.getDist();

        @Override public boolean isModLoaded(String id) {
            return net.neoforged.fml.ModList.get().isLoaded(id);
        }

        @Override public boolean isDevelopmentEnvironment() {
            return !net.neoforged.fml.loading.FMLLoader.getCurrent().isProduction();
        }

        @Override public @NonNull Path getConfigDir() {
            return net.neoforged.fml.loading.FMLPaths.CONFIGDIR.get();
        }

        @Override public boolean isClient() {
            return dist.isClient();
        }

        @Override public boolean isServer() {
            return dist.isDedicatedServer();
        }
    }
    *///?}
}