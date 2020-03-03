package athena.util.json.service.hooks;

import athena.util.json.service.hooks.annotation.PostDeserialize;
import athena.util.json.service.hooks.annotation.PreDeserialize;
import athena.util.reflection.MethodInspector;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides an adapter for post/pre deserialize operations.
 *
 * @param <T> T
 */
public final class HooksAdapterExtension<T> {

    /**
     * The method inspector for caching methods.
     */
    private final MethodInspector inspector = new MethodInspector();

    @SuppressWarnings("unchecked")
    public HooksAdapterExtension(Class<?> clazz) {
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{PostDeserialize.class});
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{PreDeserialize.class});
    }

    /**
     * Invoked before a object is serialized
     *
     * @param value the object
     */
    public void preSerialize(T value) {
        final var methods = inspector.getMethods(value.getClass(), PreDeserialize.class);
        for (final var method : methods) {
            try {
                method.invoke(value);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Invoked after the object has been deserialized.
     *
     * @param deserialized the object
     */
    public void postDeserialize(T deserialized) {
        final var methods = inspector.getMethods(deserialized.getClass(), PostDeserialize.class);

        for (final var method : methods) {
            try {
                method.invoke(deserialized);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }

}
