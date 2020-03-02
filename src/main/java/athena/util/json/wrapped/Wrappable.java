package athena.util.json.wrapped;

import athena.util.json.builder.JsonObjectBuilder;
import com.google.gson.Gson;

public final class Wrappable {

    public static void addTo(String objectName, String valueName, Object value, Gson gson, JsonObjectBuilder builder) {
        builder.add(objectName, new JsonObjectBuilder()
                .add(valueName, gson.toJson(value)).build().toString());
    }

}
