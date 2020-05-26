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

    /**
     * Initialize a new instance with hooks only.
     *
     * @param type   the type
     * @param athena athena
     * @return a new {@link AthenaServiceAdapterFactory}
     */
    public static AthenaServiceAdapterFactory withHooksOnly(Class<?> type, Athena athena) {
        return new AthenaServiceAdapterFactory(type, athena, true, false);
    }

    /**
     * Initialize a new instance with context only.
     *
     * @param type   the type
     * @param athena athena
     * @return a new {@link AthenaServiceAdapterFactory}
     */
    public static AthenaServiceAdapterFactory withContextOnly(Class<?> type, Athena athena) {
        return new AthenaServiceAdapterFactory(type, athena, false, true);
    }

    /**
     * Initialize a new instance with both services.
     *
     * @param type   the type
     * @param athena athena
     * @return a new {@link AthenaServiceAdapterFactory}
     */
    public static AthenaServiceAdapterFactory of(Class<?> type, Athena athena) {
        return new AthenaServiceAdapterFactory(type, athena, true, true);
    }

    /**
     * Initialize this service factory.
     *
     * @param clazz   the class type.
     * @param athena  athena
     * @param hooks   {@code true} if to use hooks.
     * @param context {@code true} if to use context.
     */
    private AthenaServiceAdapterFactory(Class<?> clazz, Athena athena, boolean hooks, boolean context) {
        this.clazz = clazz;
        this.athena = athena;
        this.useHooks = hooks;
        this.useContext = context;
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
