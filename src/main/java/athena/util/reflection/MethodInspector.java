package athena.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Used to cache annotated methods.
 * TODO: Currently class only supports 1 parameter type.
 */
public final class MethodInspector {

    /**
     * The cache.
     */
    private final ConcurrentMap<Class<?>, ConcurrentMap<Class<? extends Annotation>, CacheObject>> cache = new ConcurrentHashMap<>();

    /**
     * Cache annotated methods.
     *
     * @param clazz      the class
     * @param annotation the annotation.
     */
    public void cacheAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz == null || annotation == null) return;
        final var collect = collect(clazz, annotation);
        if (collect == null) return;

        final var map = new ConcurrentHashMap<Class<? extends Annotation>, CacheObject>();
        map.put(annotation, new CacheObject(collect));
        cache.put(clazz, map);
    }

    /**
     * Collect methods that are annotated with the provided {@code annotation}
     *
     * @param clazz      the class
     * @param annotation the annotation
     * @return the collection of methods or {@code null}
     */
    private Collection<Method> collect(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (clazz == null || annotation == null) return null;
        final var methods = new LinkedHashSet<Method>();

        // first collect all declared methods.
        for (var method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                method.setAccessible(true);
                methods.add(method);
            }
        }

        // next, collect super-class methods.
        final var superClassMethods = collect(clazz.getSuperclass(), annotation);
        if (superClassMethods != null) methods.addAll(superClassMethods);
        // TODO: Interface methods?
        return methods;
    }

    /**
     * Get the annotated methods from cache.
     *
     * @param clazz      the class
     * @param annotation the annotation
     * @return the collection of methods
     */
    public Collection<Method> getAnnotatedMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        final var map = cache.get(clazz);
        if (map == null) return Collections.emptyList();
        final var cache = map.get(annotation);
        return cache.methods.keySet();
    }

    /**
     * Get the annotated methods with the parameter type from cache.
     *
     * @param clazz         the class
     * @param annotation    the annotation
     * @param parameterType the parameter type class
     * @return the collection of methods
     */
    public Collection<Method> getAnnotatedMethodsWith(Class<?> clazz, Class<? extends Annotation> annotation, Class<?> parameterType) {
        final var map = cache.get(clazz);
        if (map == null) return Collections.emptyList();
        final var cache = map.get(annotation);
        final var methods = new LinkedHashSet<Method>();
        for (final var entry : cache.methods.entrySet()) {
            if (entry.getValue().isAssignableFrom(parameterType)) methods.add(entry.getKey());
        }
        return methods;
    }

    /**
     * A cache object/subscriber.
     * Holds method and parameter data.
     * TODO: Improve in the future!
     */
    private static final class CacheObject {

        /**
         * List of methods and their parameter types.
         * TODO: Only supports 1 type.
         */
        private final Map<Method, Class<?>> methods = new HashMap<>();

        private CacheObject(Collection<Method> methods) {
            methods.forEach(method -> this.methods.put(method, method.getParameterTypes().length == 0 ? null : method.getParameterTypes()[0]));
        }
    }


}
