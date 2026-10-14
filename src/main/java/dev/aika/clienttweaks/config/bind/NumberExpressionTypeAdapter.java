package dev.aika.clienttweaks.config.bind;

import com.google.gson.*;
import dev.aika.clienttweaks.util.NumberExpression;

import java.lang.reflect.Type;

public final class NumberExpressionTypeAdapter implements JsonSerializer<NumberExpression>, JsonDeserializer<NumberExpression> {
    @Override
    public NumberExpression deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        if (!json.isJsonPrimitive() || !json.getAsJsonPrimitive().isString())
            throw new JsonParseException("Expected a string");
        return NumberExpression.parse(json.getAsString());
    }

    @Override public JsonElement serialize(NumberExpression src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }
}