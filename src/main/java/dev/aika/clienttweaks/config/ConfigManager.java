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
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Supplier;

public final class ConfigManager<T> implements Supplier<T> {
    private final File CONFIG_FILE;
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
        this.CONFIG_FILE = new File(ModLoaderAccess.INSTANCE.getConfigDir().toFile(), modId + ".json");
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
        if (!CONFIG_FILE.exists()) {
            config = configClass.getConstructor().newInstance();
            save();
            return this;
        }

        try (var reader = new FileReader(CONFIG_FILE)) {
            config = GSON.fromJson(reader, configClass);
        } catch (IOException e) {
            Constants.LOG.error("Failed to read config file: {}", CONFIG_FILE, e);
        }
        return this;
    }

    public void save() {
        final var configStr = GSON.toJson(config);
        try (var writer = new FileWriter(CONFIG_FILE)) {
            writer.write(configStr);
        } catch (IOException e) {
            Constants.LOG.error("Failed to write config file {}", CONFIG_FILE, e);
        }
    }
}