package athena.util.json.hooks;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used to run actions after deserializing
 */
public final class Hooks implements TypeAdapterFactory {

    /**
     * Stores methods annotated with {@link PostDeserialize}
     */
    private final Map<Type, List<Method>> methodCache = new HashMap<>();

    /**
     * Initiate with all of the class types
     *
     * @param types the classes
     * @return a new {@link Hooks}
     */
    public static Hooks allOf(Class<?>... types) {
        return new Hooks(types);
    }

    /**
     * Initialize and populate the map
     *
     * @param types the types
     */
    private Hooks(Class<?>... types) {
        for (var clazz : types) {
            final var list = new ArrayList<Method>();
            for (var method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(PostDeserialize.class)) {
                    method.setAccessible(true);
                    list.add(method);
                }
            }
            if (!list.isEmpty()) methodCache.put(clazz, list);
        }
    }

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        if (!methodCache.containsKey(type.getRawType())) return null;
        return new TypeAdapter<>() {
            private final List<Method> methods = methodCache.get(type.getRawType());
            private final TypeAdapter<T> original = gson.getDelegateAdapter(Hooks.this, type);

            @Override
            public void write(JsonWriter out, T value) throws IOException {
                original.write(out, value);
            }

            @Override
            public T read(JsonReader in) throws IOException {
                final var deserialized = original.read(in);
                methods.forEach(method -> {
                    try {
                        method.invoke(deserialized);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                });
                return deserialized;
            }
        };
    }
}
