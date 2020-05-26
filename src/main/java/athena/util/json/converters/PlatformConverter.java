package athena.util.json.converters;

import athena.types.Platform;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Converts the type {@link Platform}
 */
public final class PlatformConverter implements JsonSerializer<Platform>, JsonDeserializer<Platform> {

    @Override
    public Platform deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Platform.typeOf(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Platform src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.primaryName());
    }
}
