package athena.util.event;

import com.esotericsoftware.reflectasm.MethodAccess;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;
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
        final var methods = clazz.getDeclaredMethods();
        final var access = MethodAccess.get(clazz);

        SubscriberMethod[] subscriberMethods = new SubscriberMethod[0];

        var hasValidMethod = false;
        for (final var method : methods) {
            // make sure our annotation is present.
            if (method.isAnnotationPresent(primaryAnnotation)) {
                final var index = access.getIndex(method.getName());
                final var parameterTypes = access.getParameterTypes()[index];
                // make sure only one param.
                if (parameterTypes.length == parameterLimit) {
                    method.setAccessible(true);
                    hasValidMethod = true;

                    // add the event method to the subscriber.
                    final var eventMethod = new SubscriberMethod(index, parameterTypes);
                    subscriberMethods = ArrayUtils.add(subscriberMethods, eventMethod);
                }
            }
        }

        // no valid method, throw.
        if (!hasValidMethod)
            throw new IllegalArgumentException("Class must include one method with the " + primaryAnnotation.getName() + " annotation and no more than " + parameterLimit + " method parameters.");

        // add subscriber
        final var subscriber = new Subscriber(subscriberMethods, access);
        subscribers.put(eventListener, subscriber);
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
        for (final var entry : subscribers.entrySet()) entry.getValue().invoke(entry.getKey(), arguments);
    }

    @Override
    public void dispose() {
        subscribers.clear();
    }

    /**
     * A event subscriber.
     * Holds methods/access and the invoke function.
     */
    private static final class Subscriber {
        private final SubscriberMethod[] methods;
        private final MethodAccess access;

        private Subscriber(SubscriberMethod[] methods, MethodAccess access) {
            this.methods = methods;
            this.access = access;
        }

        /**
         * Invoke if a method matches the provided {@code arguments}
         *
         * @param type      the type
         * @param arguments the arguments.
         */
        private void invoke(Object type, Object... arguments) {
            for (final var method : methods) {
                for (final var arg : arguments) {
                    if (method.assignable(arg.getClass())) access.invoke(type, method.index, arguments);
                }
            }
        }
    }

    /**
     * A class that stores the methodIndex, parameters and the object register.
     */
    private static final class SubscriberMethod {

        private final Class<?>[] parameterTypes;
        private final int index;

        private SubscriberMethod(int index, Class<?>[] parameterTypes) {
            this.index = index;
            this.parameterTypes = parameterTypes;
        }

        /**
         * Check if the provided {@code type} is {@code isAssignableFrom} to the method parameter types.
         *
         * @param type the type
         * @return {@code true} if its assignable from.
         */
        private boolean assignable(Class<?> type) {
            for (final var parameter : parameterTypes) if (parameter.isAssignableFrom(type)) return true;
            return false;
        }

        /**
         * @return the method index.
         */
        private int index() {
            return index;
        }
    }

}
