package athena.util.json.builder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * Utility class to build {@link com.google.gson.JsonObject}s
 */
public final class JsonObjectBuilder {

    private JsonObject jsonObject = new JsonObject();

    public JsonObjectBuilder add(String key, String value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder add(String key, Number value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder add(String key, Boolean value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonObjectBuilder add(String key, JsonElement value) {
        jsonObject.add(key, value);
        return this;
    }

    public JsonObject build() {
        return jsonObject;
    }

    public void clear() {
        jsonObject = new JsonObject();
    }

    public boolean isEmpty() {
        return jsonObject.size() == 0;
    }

}
