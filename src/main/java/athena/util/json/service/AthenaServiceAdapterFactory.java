package athena.util.json.service;

import athena.Athena;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

/**
 * Provides a factory for all services such as: hooks, context and wrapped.
 */
public final class AthenaServiceAdapterFactory implements TypeAdapterFactory {

    /**
     * Class type.
     */
    private final Class<?> clazz;

    /**
     * The athena instance
     */
    private final Athena athena;

    /**
     * Properties
     */
    private boolean useHooks, useContext;

    public AthenaServiceAdapterFactory(Class<?> clazz, Athena athena) {
        this.clazz = clazz;
        this.athena = athena;
    }

    /**
     * Set to use hooks.
     *
     * @return this
     */
    public AthenaServiceAdapterFactory useHooks() {
        this.useHooks = true;
        return this;
    }

    /**
     * Set to use context
     *
     * @return this
     */
    public AthenaServiceAdapterFactory useContext() {
        this.useContext = true;
        return this;
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (type.getRawType().isAssignableFrom(clazz)) {
            // grab our original adapter.
            final var original = gson.getDelegateAdapter(this, type);
            return new AthenaServiceAdapter<>(clazz, original, athena, useHooks, useContext);
        } else {
            return null;
        }
    }
}
