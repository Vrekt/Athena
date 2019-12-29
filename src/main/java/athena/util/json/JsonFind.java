package athena.util.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.Optional;

/**
 * A utility class for finding JSON values or returning a default, like 0, "", null, etc.
 */
public final class JsonFind {

    /**
     * An empty object used for returning when a object can't be found in the provided json.
     */
    private static final JsonObject EMPTY_OBJECT = new JsonObject();

    /**
     * An empty array used for returning when a array can't be found in the provided json.
     */
    private static final JsonArray EMPTY_ARRAY = new JsonArray();

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return a {@link Optional} that will contain the value if found.
     */
    public static Optional<String> findStringOptional(JsonObject object, String find) {
        if (object == null) return Optional.empty();
        final var get = object.get(find);
        return Optional.ofNullable(get == null ? null : get.getAsString());
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value(s) to find
     * @return a {@link Optional} that will contain the value if found.
     */
    public static Optional<String> findStringsOptional(JsonObject object, String... find) {
        if (object == null) return Optional.empty();
        for (final var f : find) {
            if (object.has(f)) return Optional.ofNullable(object.get(f).getAsString());
        }
        return Optional.empty();
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return a string that will not be empty if the value is found.
     */
    public static String findString(JsonObject object, String find) {
        return findStringOptional(object, find).orElse("");
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return a {@link Optional} that will contain the value if found.
     */
    public static Optional<Integer> findIntOptional(JsonObject object, String find) {
        if (object == null) return Optional.empty();
        final var get = object.get(find);
        return Optional.ofNullable(get == null ? null : get.getAsInt());
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return the int, or 0.
     */
    public static int findInt(JsonObject object, String find) {
        return findIntOptional(object, find).orElse(0);
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return a {@link Optional} that will contain the value if found.
     */
    public static Optional<JsonArray> findArrayOptional(JsonObject object, String find) {
        if (object == null) return Optional.empty();
        final var get = object.get(find);
        return Optional.ofNullable(get == null ? null : get.getAsJsonArray());
    }

    /**
     * Attempts to find the value in the {@code object}
     *
     * @param object the object to search
     * @param find   the value to find
     * @return an array
     */
    public static JsonArray findArray(JsonObject object, String find) {
        return findArrayOptional(object, find).orElse(EMPTY_ARRAY);
    }

}
