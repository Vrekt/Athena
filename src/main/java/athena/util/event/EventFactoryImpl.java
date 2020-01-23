package athena.util.event;

import athena.util.reflection.MethodInspector;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ConcurrentHashMap;

public final class EventFactoryImpl implements EventFactory {

    /**
     * A map of subscribers.
     */
    private final ConcurrentHashMap<Object, Subscriber> subscribers = new ConcurrentHashMap<>();

    /**
     * Primary annotation for methods and the method parameter limit.
     */
    private final Class<? extends Annotation> primaryAnnotation;
    private final int parameterLimit;

    EventFactoryImpl(Class<? extends Annotation> primaryAnnotation, int parameterLimit) {
        this.primaryAnnotation = primaryAnnotation;
        this.parameterLimit = parameterLimit;
    }

    EventFactoryImpl(EventFactoryImpl other) {
        subscribers.putAll(other.subscribers);
        primaryAnnotation = other.primaryAnnotation;
        parameterLimit = other.parameterLimit;
    }

    @Override
    public void registerEventListener(Object eventListener) {
        final var clazz = eventListener.getClass();
        final var inspector = new MethodInspector();
        inspector.cacheAnnotatedMethods(clazz, primaryAnnotation);
        subscribers.put(eventListener, new Subscriber(clazz, inspector));
    }

    @Override
    public void unregisterEventListener(Object eventListener) {
        subscribers.remove(eventListener);
    }

    /**
     * TODO: Store key by parameter types so we don't have to iterate through whole map?
     *
     * @param arguments the arguments.
     */
    @Override
    public void invoke(Object... arguments) {
        for (final var entry : subscribers.entrySet()) {
            final var subscriber = entry.getValue();
            invoke(subscriber.clazz, subscriber.inspector, entry.getKey(), arguments);
        }
    }

    @Override
    public void dispose() {
        subscribers.clear();
    }

    private void invoke(Class<?> clazz, MethodInspector inspector, Object type, Object... arguments) {
        final var methods = inspector.getAnnotatedMethodsWith(clazz, primaryAnnotation, arguments[0].getClass());
        for (final var method : methods) {
            try {
                method.invoke(type, arguments);
            } catch (IllegalAccessException | InvocationTargetException exception) {
                exception.printStackTrace();
                // TODO: Logger?
            }
        }
    }

    /**
     * Represents an event subscriber.
     */
    private static final class Subscriber {
        /**
         * The inspector
         */
        private final MethodInspector inspector;
        /**
         * Base class
         */
        private final Class<?> clazz;

        private Subscriber(Class<?> clazz, MethodInspector inspector) {
            this.clazz = clazz;
            this.inspector = inspector;
        }
    }

}
