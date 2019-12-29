package athena.adapter;

import athena.Athena;
import com.google.gson.JsonDeserializer;

/**
 * Used for services that have to adapt to certain objects via GSON.
 *
 * @param <T> the TYPE
 */
public interface ObjectJsonAdapter<T> extends JsonDeserializer<T> {

    /**
     * Initialize the adapter with the local {@link Athena} instance.
     * Used for cases where (for example) accounts need the friends public service and local account ID.
     *
     * @param athena the athena instance.
     */
    void initialize(Athena athena);

}
