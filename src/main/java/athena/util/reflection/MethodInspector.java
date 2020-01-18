package athena.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Used to cache annotated methods.
 */
public final class MethodInspector {

    /**
     * The cache.
     */
    private final ConcurrentMap<Class<?>, ConcurrentMap<Class<? extends Annotation>, Collection<Method>>> cache = new ConcurrentHashMap<>();

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

        final var map = new ConcurrentHashMap<Class<? extends Annotation>, Collection<Method>>();
        map.put(annotation, collect);
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
        return map == null ? Collections.emptyList() : map.get(annotation);
    }
}
