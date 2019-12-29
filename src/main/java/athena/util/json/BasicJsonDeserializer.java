package athena.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * A utility class to remove the extra two parameters from the deserialize method in {@link JsonDeserializer}
 *
 * @param <T> TYPE
 */
public interface BasicJsonDeserializer<T> extends JsonDeserializer<T> {

    @Override
    default T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return deserialize(json);
    }

    T deserialize(JsonElement json);
}
