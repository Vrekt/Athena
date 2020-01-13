package athena.util.event;

import java.lang.annotation.Annotation;

public interface EventFactory {

    /**
     * Create a new event factory.
     *
     * @param primaryAnnotation the primary annotation.
     * @param parameterLimit    the parameter limit for methods
     * @return a new {@link EventFactory}
     */
    static EventFactory create(Class<? extends Annotation> primaryAnnotation, int parameterLimit) {
        return new EventFactoryImpl(primaryAnnotation, parameterLimit);
    }

    /**
     * Create a new event factory from another.
     *
     * @param other the other
     * @return a new {@link EventFactory}
     */
    static EventFactory create(EventFactory other) {
        return new EventFactoryImpl((EventFactoryImpl) other);
    }

    /**
     * Register an event listener.
     *
     * @param eventListener the class/type to register.
     */
    void registerEventListener(Object eventListener);

    /**
     * Unregister an event listener.
     *
     * @param eventListener the class/type to register.
     */
    void unregisterEventListener(Object eventListener);

    /**
     * Invoke an event type.
     *
     * @param arguments the arguments.
     */
    void invoke(Object... arguments);

    /**
     * Dispose of this factory.
     */
    void dispose();

}
