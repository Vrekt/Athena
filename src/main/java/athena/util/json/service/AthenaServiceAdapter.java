package athena.util.json.service;

import athena.Athena;
import athena.util.json.service.context.ContextAdapterExtension;
import athena.util.json.service.hooks.HooksAdapterExtension;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Provides the adapter for the various services.
 * TODO: Wrapped at some point if possible.
 *
 * @param <T>
 */
public final class AthenaServiceAdapter<T> extends TypeAdapter<T> {

    /**
     * The original adapter.
     */
    private final TypeAdapter<T> adapter;
    /**
     * Settings
     */
    private final boolean useHooks, useContext;

    /**
     * The extensions
     */
    private ContextAdapterExtension<T> contextExtension;
    private HooksAdapterExtension<T> hooksExtension;

    public AthenaServiceAdapter(Class<?> clazz, TypeAdapter<T> original, Athena athena, boolean useHooks, boolean useContext) {
        this.adapter = original;
        this.useHooks = useHooks;
        this.useContext = useContext;

        if (useContext) contextExtension = new ContextAdapterExtension<>(clazz, athena);
        if (useHooks) hooksExtension = new HooksAdapterExtension<>(clazz);
    }

    @Override
    public void write(JsonWriter out, T value) throws IOException {
        adapter.write(out, value);
    }

    @Override
    public T read(JsonReader in) throws IOException {
        final var deserialized = adapter.read(in);
        if (useContext) contextExtension.postDeserialize(deserialized);
        if (useHooks) hooksExtension.postDeserialize(deserialized);
        return deserialized;
    }
}
