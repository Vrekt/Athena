package athena.util.json.hooks;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Returns a new {@link HooksAdapter} for the type
 */
public final class HooksAdapterFactory implements TypeAdapterFactory {

    /**
     * Class type.
     */
    private final Class<?> clazz;

    public HooksAdapterFactory(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(clazz)) {
            final var original = gson.getDelegateAdapter(this, type);
            return new HooksAdapter<>(clazz, original);
        } else {
            return null;
        }
    }
}
