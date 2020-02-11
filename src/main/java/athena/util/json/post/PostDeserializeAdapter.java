package athena.util.json.post;

import athena.util.json.post.annotation.PostDeserialize;
import athena.util.reflection.MethodInspector;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * Provides an adapter for post deserialize operations.
 *
 * @param <T> T
 */
public final class PostDeserializeAdapter<T> extends TypeAdapter<T> {

    /**
     * The method inspector for caching methods.
     */
    private final MethodInspector inspector = new MethodInspector();
    /**
     * The original adapter.
     */
    private final TypeAdapter<T> adapter;

    @SuppressWarnings("unchecked")
    public PostDeserializeAdapter(Class<?> clazz, TypeAdapter<T> adapter) {
        this.adapter = adapter;

        // collect all methods annotated and cache them.
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{PostDeserialize.class});
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
