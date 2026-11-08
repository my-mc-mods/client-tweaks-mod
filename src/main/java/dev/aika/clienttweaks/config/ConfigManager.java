package dev.aika.clienttweaks.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.aika.clienttweaks.Constants;
import dev.aika.clienttweaks.config.bind.ColorTypeAdapter;
import dev.aika.clienttweaks.config.bind.NumberExpressionTypeAdapter;
import dev.aika.clienttweaks.util.ModLoaderAccess;
import dev.aika.clienttweaks.util.NumberExpression;
import lombok.Getter;
import lombok.SneakyThrows;

import java.awt.*;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

public final class ConfigManager<T> implements Supplier<T> {
    @Getter
    private final Path configDirectory;
    @Getter
    private final Path configFile;
    private final Path legacyConfigFile;
    private T config;
    private final Class<T> configClass;
    @Getter
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Color.class, new ColorTypeAdapter())
            .registerTypeAdapter(NumberExpression.class, new NumberExpressionTypeAdapter())
            .create();

    private ConfigManager(Class<T> configClass, String modId) {
        this.configClass = configClass;
        final Path root = ModLoaderAccess.INSTANCE.getConfigDir();
        this.configDirectory = root.resolve(modId);
        this.configFile = this.configDirectory.resolve(modId + ".json");
        this.legacyConfigFile = root.resolve(modId + ".json");
    }

    public static <T> ConfigManager<T> create(Class<T> configClass, String modId) {
        return new ConfigManager<>(configClass, modId);
    }

    @Override public T get() {
        return config;
    }

    @SuppressWarnings("UnusedReturnValue")
    @SneakyThrows
    public ConfigManager<T> load() {
        migrateLegacyConfigIfNeeded();

        if (!Files.exists(configFile)) {
            config = configClass.getConstructor().newInstance();
            save();
            return this;
        }

        try (Reader reader = Files.newBufferedReader(configFile)) {
            config = GSON.fromJson(reader, configClass);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file: {}", configFile, e);
        }
        return this;
    }

    public void save() {
        final var configStr = GSON.toJson(config);
        try {
            Files.createDirectories(configDirectory);
            try (Writer writer = Files.newBufferedWriter(configFile)) {
                writer.write(configStr);
            }
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file {}", configFile, e);
        }
    }

    private void migrateLegacyConfigIfNeeded() {
        if (Files.exists(configFile) || !Files.exists(legacyConfigFile)) return;

        try {
            Files.createDirectories(configDirectory);
            Files.move(legacyConfigFile, configFile);
            Constants.LOG.info("Migrated config from {} to {}", legacyConfigFile, configFile);
        } catch (IOException e) {
            Constants.LOG.error("Failed to migrate config from {} to {}", legacyConfigFile, configFile, e);
        }
    }
}
