package athena.util.json.service.context;

import athena.Athena;
import athena.util.json.service.context.annotation.Context;
import athena.util.reflection.MethodInspector;

import java.lang.reflect.InvocationTargetException;

/**
 * Provides an adapter for setting local {@link Athena} instance after deserializing.
 *
 * @param <T> T
 */
public final class ContextAdapterExtension<T> {

    /**
     * The method inspector for caching methods.
     */
    private final MethodInspector inspector = new MethodInspector();
    /**
     * Local athena instance.
     */
    private final Athena athena;

    @SuppressWarnings("unchecked")
    public ContextAdapterExtension(Class<?> clazz, Athena athena) {
        this.athena = athena;
        inspector.cacheAnnotatedMethodsOnce(clazz, new Class[]{Context.class});
    }

    /**
     * Invoked after the object has been deserialized.
     */
    public void postDeserialize(T deserialized) {
        final var methods = inspector.getMethodsWithParameters(deserialized.getClass(), Context.class, Athena.class);
        for (final var method : methods) {
            try {
                method.invoke(deserialized, athena);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }

}
