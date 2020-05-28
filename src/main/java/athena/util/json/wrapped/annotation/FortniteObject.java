package athena.util.json.wrapped.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * * Represents a 'FortniteObject'
 * * A JSON value that is wrapped inside a string.
 * For example:
 * <p>
 * "MyValue_j" : "{\"MyValue\":123}",
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FortniteObject {
    /**
     * The wrapped name.
     * Using the example above this value would be "MyValue"
     *
     * @return the value
     */
    String value();

}
