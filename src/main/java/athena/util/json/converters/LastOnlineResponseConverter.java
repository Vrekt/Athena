package athena.util.json.converters;

import athena.presence.resource.LastOnlineResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;
import java.util.HashMap;

/**
 * Used to deserialize {@link LastOnlineResponse}
 */
public final class LastOnlineResponseConverter implements JsonDeserializer<LastOnlineResponse> {

    @Override
    public LastOnlineResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final var object = json.getAsJsonObject();
        final var map = new HashMap<String, Instant>();

        // iterate through each key and grab the array.
        object.keySet().forEach(key -> {
            final var array = object.getAsJsonArray(key);
            final var lastOnline = array.get(0).getAsJsonObject().get("last_online");
            final var time = Instant.parse(lastOnline.getAsString());
            map.put(key, time);
        });
        return new LastOnlineResponse(map);
    }
}
