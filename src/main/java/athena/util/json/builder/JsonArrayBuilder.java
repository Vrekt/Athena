package athena.util.json.builder;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

/**
 * Utility class to build {@link com.google.gson.JsonArray}s
 */
public final class JsonArrayBuilder {

    private final JsonArray jsonArray = new JsonArray();

    public JsonArrayBuilder add(String value) {
        jsonArray.add(value);
        return this;
    }

    public JsonArrayBuilder add(Number value) {
        jsonArray.add(value);
        return this;
    }

    public JsonArrayBuilder add(Boolean value) {
        jsonArray.add(value);
        return this;
    }

    public JsonArrayBuilder add(JsonElement value) {
        jsonArray.add(value);
        return this;
    }

    public JsonArray build() {
        return jsonArray;
    }

}
