package athena.util.json.request;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to request classes/services/resources
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Request {

    /**
     * The item to request.
     */
    Class<?> item();

    /**
     * Indicates if the value is the local account.
     *
     * @return {@code true} if so.
     */
    boolean local() default false;

}
