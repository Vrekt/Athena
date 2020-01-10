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
