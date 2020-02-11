package athena.util.json.converters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

/**
 * Used to serialize and de-serialize {@link Instant}
 */
public final class InstantConverter implements JsonSerializer<Instant>, JsonDeserializer<Instant> {

    /**
     * The formatter for parsing dates.
     */
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_INSTANT;

    @Override
    public Instant deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return Instant.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(FORMATTER.format(src));
    }
}
