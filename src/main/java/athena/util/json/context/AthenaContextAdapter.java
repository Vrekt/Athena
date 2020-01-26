package athena.util.json.context;

import athena.Athena;
import athena.context.DefaultAthenaContext;
import athena.util.json.context.annotation.Context;
import athena.util.reflection.MethodInspector;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides an adapter for setting local {@link Athena} instance after deserializing.
 *
 * @param <T> T
 */
public final class AthenaContextAdapter<T> extends TypeAdapter<T> {

    /**
     * The method inspector for caching methods.
     */
    private final MethodInspector inspector = new MethodInspector();
    /**
     * The original adapter.
     */
    private final TypeAdapter<T> adapter;
    /**
     * Local athena instance.
     */
    private final Athena athena;

    @SuppressWarnings("unchecked")
    public AthenaContextAdapter(Class<?> clazz, TypeAdapter<T> adapter, Athena athena) {
        this.adapter = adapter;
        this.athena = athena;

        // collect all methods annotated and cache them.
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{Context.class});
    }

    /**
     * TODO: May be needed in future.
     *
     * @param out   out
     * @param value value
     */
    @Override
    public void write(JsonWriter out, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T read(JsonReader in) throws IOException {
        final var deserialized = adapter.read(in);
        final var methods = inspector.getMethodsWithParameters(deserialized.getClass(), Context.class, DefaultAthenaContext.class);

        for (final var method : methods) {
            try {
                method.invoke(deserialized, athena);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
        return deserialized;
    }


}
