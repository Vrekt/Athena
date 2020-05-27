package athena.util.json.wrapped.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates if the field value is wrapped inside a json string, the wrapped value must be a JsonArray.
 * For example:
 *
 * @WrappedArray("MyValue") "MyValue_j" : "{\"MyValue\":[]}",
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface WrappedArray {
    /**
     * The wrapped name.
     * Using the example above this value would be "MyValue"
     *
     * @return the value
     */
    String value();

    /**
     * The type of object.
     *
     * @return the type
     */
    Class<?> type();

    /**
     * Used for objects that are always constant values.
     * For example empty JSON arrays.
     *
     * @return if the value is constant.
     */
    boolean isConstant() default false;

    /**
     * Identifies if this type is NOT a list.
     *
     * @return if this type is not a list.
     */
    boolean isNotList() default false;

}
