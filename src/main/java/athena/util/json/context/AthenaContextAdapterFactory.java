package athena.util.json.context;

import athena.Athena;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Returns a new {@link AthenaContextAdapter} for the type.
 */
public final class AthenaContextAdapterFactory implements TypeAdapterFactory {

    /**
     * Local athena instance.
     */
    private final Athena athena;
    /**
     * Class type.
     */
    private final Class<?> clazz;

    public AthenaContextAdapterFactory(Class<?> clazz, Athena athena) {
        this.athena = athena;
        this.clazz = clazz;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(clazz)) {
            final var original = gson.getDelegateAdapter(this, type);
            return new AthenaContextAdapter<>(clazz, original, athena);
        } else {
            return null;
        }
    }
}
