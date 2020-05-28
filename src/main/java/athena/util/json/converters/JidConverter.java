package athena.util.json.converters;

import com.google.gson.*;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;

import java.lang.reflect.Type;

/**
 * Converts the type {@link org.jxmpp.jid.Jid}
 */
public final class JidConverter implements JsonSerializer<Jid>, JsonDeserializer<Jid> {

    @Override
    public Jid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return JidCreate.fromOrNull(json.getAsJsonPrimitive().getAsString());
    }

    @Override
    public JsonElement serialize(Jid src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.asUnescapedString());
    }
}
