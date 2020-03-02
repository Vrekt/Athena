package athena.util.json.wrapped.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates if the field value is wrapped inside a json string, the wrapped value must be a JsonObject.
 * For example:
 *
 * @WrappedObject("MyValue") "MyValue_j" : "{\"MyValue\":123}",
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WrappedObject {
    /**
     * The wrapped name.
     * Using the example above this value would be "MyValue"
     *
     * @return the value
     */
    String value();

}
