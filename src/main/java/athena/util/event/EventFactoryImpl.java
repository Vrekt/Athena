package athena.util.event;

import athena.util.reflection.MethodInspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

final class EventFactoryImpl implements EventFactory {

    private final Class<? extends Annotation>[] annotations;

    private final ConcurrentHashMap<Object, EventSubscriber> subscribers = new ConcurrentHashMap<>();

    @SafeVarargs
    EventFactoryImpl(Class<? extends Annotation>... annotations) {
        this.annotations = annotations;
    }

    @Override
    public void registerEventListener(Object eventListener) {
        final var clazz = eventListener.getClass();
        subscribers.put(eventListener, new EventSubscriber(clazz, annotations));
    }

    @Override
    public void unregisterEventListener(Object eventListener) {
        final var subscriber = subscribers.get(eventListener);
        subscriber.inspector.removeClass(subscriber.clazz);
        subscribers.remove(eventListener, subscriber);
    }

    @Override
    public void unregisterAll() {
        subscribers.values().forEach(eventSubscriber -> eventSubscriber.inspector.removeClass(eventSubscriber.clazz));
        subscribers.clear();
    }

    @Override
    public void invoke(Class<? extends Annotation> annotation, Object... arguments) {
        for (final var entry : subscribers.entrySet()) {
            final var subscriber = entry.getValue();
            invoke(subscriber.clazz, annotation, subscriber.inspector, entry.getKey(), arguments);
        }
    }

    /**
     * Invoke a method.
     *
     * @param clazz      the class
     * @param annotation the annotation
     * @param inspector  the inspector
     * @param type       the original object
     * @param arguments  arguments
     */
    private void invoke(Class<?> clazz, Class<? extends Annotation> annotation, MethodInspector inspector, Object type, Object... arguments) {
        final var classes = new Class<?>[arguments.length];
        for (int i = 0; i < arguments.length; i++) {
            classes[i] = arguments[i].getClass();
        }

        final var methods = arguments.length == 0 ? inspector.getMethods(clazz, annotation) : inspector.getMethodsWithParameters(clazz, annotation, classes);
        for (final var method : methods) {
            try {
                method.invoke(type, arguments);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
            }
        }
    }

    @Override
    public void dispose() {
        unregisterAll();
    }

    /**
     * Represents an event subscriber from {@code registerEventListener}
     */
    private static final class EventSubscriber {

        /**
         * The method inspector.
         */
        private final MethodInspector inspector = new MethodInspector();
        /**
         * The class that belongs to this subscriber.
         */
        private final Class<?> clazz;

        private EventSubscriber(Class<?> clazz, Class<? extends Annotation>[] annotations) {
            this.clazz = clazz;

            inspector.cacheAnnotatedMethodsOnce(clazz, annotations);
        }
    }

}
