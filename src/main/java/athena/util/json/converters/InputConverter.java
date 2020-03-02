package athena.util.json.converters;

import athena.types.Input;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Used for serializing and deserializing the input type.
 */
public final class InputConverter implements JsonSerializer<Input>, JsonDeserializer<Input> {

    @Override
    public Input deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Input.typeOf(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Input src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.getNames().get(0));
    }
}
