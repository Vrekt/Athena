package athena.util.json.converters;

import athena.types.Region;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Converts the type {@link athena.types.Region}
 */
public final class RegionConverter implements JsonSerializer<Region>, JsonDeserializer<Region> {

    @Override
    public Region deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Region.valueOf(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Region src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.name());
    }
}
