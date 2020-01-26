package athena.util.event;

import java.lang.annotation.Annotation;

/**
 * Used to dispatch listener/annotated type events.
 */
public interface EventFactory {

    /**
     * Creates a new annotated factory.
     *
     * @param annotations the set of annotations that are valid.
     * @return a new {@link EventFactory}
     */
    @SafeVarargs
    static EventFactory createAnnotatedFactory(Class<? extends Annotation>... annotations) {
        return new EventFactoryImpl(annotations);
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
     * Unregister all event listeners.
     */
    void unregisterAll();

    /**
     * Invoke an event type.
     *
     * @param annotation the annotation
     * @param arguments  the arguments.
     */
    void invoke(Class<? extends Annotation> annotation, Object... arguments);

    /**
     * Dispose of this factory.
     */
    void dispose();

}
