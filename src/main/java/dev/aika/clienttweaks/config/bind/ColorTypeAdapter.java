package dev.aika.clienttweaks.config.bind;

import com.google.gson.*;
import dev.aika.clienttweaks.util.ColorUtil;

import java.awt.*;
import java.lang.reflect.Type;

public final class ColorTypeAdapter implements JsonSerializer<Color>, JsonDeserializer<Color> {
    @Override
    public Color deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonObject()) throw new JsonParseException("Invalid color serialization");
        final var jsonObject = json.getAsJsonObject();
        return ColorUtil.fromHex(
                jsonObject.get("color").getAsString(),
                jsonObject.get("opacity").getAsFloat()
        );
    }

    @Override public JsonElement serialize(Color src, Type typeOfSrc, JsonSerializationContext context) {
        final var obj = new JsonObject();
        obj.add("color", new JsonPrimitive(String.format("#%06X", ColorUtil.toRgb(src))));
        obj.add("opacity", new JsonPrimitive(src.getAlpha() / 255.0f));
        return obj;
    }
}