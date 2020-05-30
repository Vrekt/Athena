package athena.util.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A utility class used to inspect and cache methods.
 */
public final class MethodInspector {

    /**
     * The method cache.
     */
    private final Map<Class<?>, Map<Class<? extends Annotation>, MethodData>> cache = new HashMap<>();

    /**
     * Cache annotated methods annotated with the provided {@code annotations} once.
     *
     * @param clazz       the class
     * @param annotations the annotations
     */
    public void cacheAnnotatedMethodsOnce(Class<?> clazz, Class<? extends Annotation>[] annotations) {
        if (clazz == null || annotations == null) throw new NullPointerException("Clazz or annotation is null.");

        final var cache = this.cache.containsKey(clazz) ? this.cache.get(clazz) : new HashMap<Class<? extends Annotation>, MethodData>();
        for (var annotation : annotations) {
            if (annotation == null) throw new NullPointerException("one of the annotations is null.");
            final var methods = collectAnnotatedMethods(new Class[]{clazz}, annotation);
            if (methods.isEmpty()) continue;
            cache.put(annotation, new MethodData(methods));
        }
        this.cache.put(clazz, cache);
    }

    /**
     * Get a list of methods annotated with the provided {@code annotation}
     *
     * @param clazz      the class
     * @param annotation the annotation
     * @return a list of methods
     */
    public Collection<Method> getMethods(Class<?> clazz, Class<? extends Annotation> annotation) {
        if (!cache.containsKey(clazz)) throw new IllegalArgumentException("That class is not cached yet.");
        final var cache = this.cache.get(clazz);
        final var data = cache.get(annotation);
        if (data == null) return Collections.emptyList();
        return data.getMethods();
    }

    /**
     * Get a list of methods annotated with the provided {@code annotation} that have the matching parameter types.
     *
     * @param clazz      the class
     * @param annotation the annotation
     * @param parameters the parameter types
     * @return a list of methods
     */
    public Collection<Method> getMethodsWithParameters(Class<?> clazz, Class<? extends Annotation> annotation, Class<?>... parameters) {
        if (!cache.containsKey(clazz)) throw new IllegalArgumentException("That class is not cached yet.");
        final var cache = this.cache.get(clazz);
        final var data = cache.get(annotation);
        return data.getMatchingParameterType(parameters);
    }

    /**
     * Remove a class from the cache.
     *
     * @param clazz the class.
     */
    public void removeClass(Class<?> clazz) {
        cache.remove(clazz);
    }

    /**
     * Collects methods annotated with the provided {@code annotation} to the {@code methods} collection
     *
     * @param annotation the annotation
     * @param methods    the collection
     * @param classes    the classes
     * @return a list of methods
     */
    private Collection<Method> collectAnnotatedMethodsTo(Class<? extends Annotation> annotation, Collection<Method> methods, Class<?>... classes) {
        for (var clazz : classes) {
            for (var method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    method.setAccessible(true);
                    methods.add(method);
                }
            }
        }

        return methods;
    }

    /**
     * Collect annotated methods
     *
     * @param classes    the classes
     * @param annotation the annotation
     * @return a list of methods
     */
    private Collection<Method> collectAnnotatedMethods(Class<?>[] classes, Class<? extends Annotation> annotation) {
        final var methods = collectAnnotatedMethodsTo(annotation, new LinkedHashSet<>(), classes);
        for (var clazz : classes) {
            methods.addAll(collectAnnotatedMethodsTo(annotation, methods, clazz.getSuperclass()));
            methods.addAll(collectAnnotatedMethodsTo(annotation, methods, clazz.getInterfaces()));
        }
        return methods;
    }

    /**
     * Used to store data about a method.
     */
    private static final class MethodData {

        /**
         * Collection of method parameter types in a map.
         */
        private final Map<ParameterData, Method> methodParameterTypes = new HashMap<>();

        private MethodData(Collection<Method> methods) {
            methods.forEach(method -> methodParameterTypes.put(new ParameterData(method.getParameterTypes()), method));
        }

        /**
         * Collects methods with the matched parameter types.
         *
         * @param types the types
         * @return a list of methods
         */
        private Collection<Method> getMatchingParameterType(Class<?>... types) {
            final var parameterData = methodParameterTypes.keySet();
            final var length = types.length;
            return parameterData
                    .stream()
                    .filter(data -> data.count == length)
                    .filter(data -> data.assignable(types))
                    .map(methodParameterTypes::get)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        /**
         * @return get all methods stored
         */
        private Collection<Method> getMethods() {
            return methodParameterTypes.values();
        }

        /**
         * Stores parameter info
         */
        private static final class ParameterData {
            /**
             * The parameter types
             */
            private final Class<?>[] parameters;
            /**
             * Amount of types
             */
            private final int count;

            private ParameterData(Class<?>[] parameters) {
                this.parameters = parameters;
                this.count = parameters.length;
            }

            /**
             * @param types the types
             * @return {@code true} if each class in {@code types} is assignable from {@code parameters}
             */
            private boolean assignable(Class<?>... types) {
                for (int i = 0; i < types.length; i++) {
                    if (!types[i].isAssignableFrom(parameters[i])) return false;
                }
                return true;
            }

        }

    }

}
