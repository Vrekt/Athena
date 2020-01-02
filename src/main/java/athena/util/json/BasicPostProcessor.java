package athena.util.json;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import io.gsonfire.PostProcessor;

/**
 * Utility class to only implement {@code postDeserialize} in {@link io.gsonfire.PostProcessor}
 */
public interface BasicPostProcessor<T> extends PostProcessor<T> {

    @Override
    default void postSerialize(JsonElement result, T src, Gson gson) {
        //
    }

}
