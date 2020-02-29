package athena.util.json.hooks;

import athena.util.json.hooks.annotation.PostDeserialize;
import athena.util.json.hooks.annotation.PreDeserialize;
import athena.util.reflection.MethodInspector;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides an adapter for post/pre deserialize operations.
 *
 * @param <T> T
 */
public final class HooksAdapter<T> extends TypeAdapter<T> {

    /**
     * The method inspector for caching methods.
     */
    private final MethodInspector inspector = new MethodInspector();
    /**
     * The original adapter.
     */
    private final TypeAdapter<T> adapter;

    @SuppressWarnings("unchecked")
    public HooksAdapter(Class<?> clazz, TypeAdapter<T> adapter) {
        this.adapter = adapter;

        // collect all methods annotated and cache them.
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{PostDeserialize.class});
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{PreDeserialize.class});
    }

    /**
     * @param out   out
     * @param value value
     */
    @Override
    public void write(JsonWriter out, T value) throws IOException {
        final var methods = inspector.getMethods(value.getClass(), PreDeserialize.class);
        for (final var method : methods) {
            try {
                method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }

        adapter.write(out, value);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        final var deserialized = adapter.read(in);
        final var methods = inspector.getMethods(deserialized.getClass(), PostDeserialize.class);

        for (final var method : methods) {
            try {
                method.invoke(deserialized);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
        return deserialized;
    }

}
