package athena.util.json.wrapped;

import athena.util.json.wrapped.annotation.WrappedArray;
import athena.util.json.wrapped.annotation.WrappedObject;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Used for classes that have to deserialize objects type in JSON strings.
 */
public final class WrappedTypeAdapterFactory implements TypeAdapterFactory {

    /**
     * A list of all type fields by the annotations {@link WrappedArray} and {@link WrappedObject}
     */
    private final Map<Type, WrappedField> fields = new HashMap<>();

    /**
     * Initialize
     *
     * @param baseType the base type class.
     */
    private WrappedTypeAdapterFactory(Class<?> baseType) {

        // Add our list of typeObject field types.
        FieldUtils.getFieldsListWithAnnotation(baseType, WrappedObject.class)
                .forEach(field -> {
                    final var annotation = field.getAnnotation(WrappedObject.class);
                    fields.put(field.getType(), new WrappedField(field.getType(), null, annotation.value(), false));
                });

        // Add our list of WrappedArray field types.
        // Register each type with either the annotation type if useRawTypes == true otherwise parameterize it with List.class
        FieldUtils.getFieldsListWithAnnotation(baseType, WrappedArray.class)
                .forEach(field -> {
                    final var annotation = field.getAnnotation(WrappedArray.class);
                    final var type = annotation.useRawType() ? annotation.type() : TypeToken.getParameterized(List.class, annotation.type()).getType();
                    fields.put(type, new WrappedField(annotation.type(), annotation.type(), annotation.value(), annotation.useRawType()));
                });
    }

    /**
     * Factory method to create new instances of this type adapter.
     *
     * @param baseType the base type to register for.
     * @return a new {@link WrappedTypeAdapterFactory}
     */
    public static WrappedTypeAdapterFactory of(Class<?> baseType) {
        return new WrappedTypeAdapterFactory(baseType);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!fields.containsKey(type.getType())) return null;

        // retrieve our field
        final var field = fields.get(type.getType());
        return new TypeAdapter<>() {

            final TypeAdapter<R> fieldTypeAdapter = (TypeAdapter<R>) gson.getDelegateAdapter(WrappedTypeAdapterFactory.this, TypeToken.get(field.fieldType));
            final TypeAdapter<R> arrayTypeAdapter = field.arrayType == null ? null : (TypeAdapter<R>) gson.getDelegateAdapter(WrappedTypeAdapterFactory.this, TypeToken.get(field.arrayType));

            @Override
            public void write(JsonWriter out, R value) throws IOException {

                // we have no value so pass this onto the original delegate adapter.
                if (value == null) {
                    fieldTypeAdapter.write(out, null);
                    return;
                }

                if (field.arrayType == null) {
                    // we are writing an object.
                    final var asString = fieldTypeAdapter.toJson(value);
                    final var object = new JsonObject();
                    object.addProperty(field.wrappedValue, asString);
                    gson.toJson(object, out);
                } else {
                    // find the adapter to use.
                    // if we are writing a list value then grab the delegate adapter for that.
                    // otherwise, use the field type adapter.
                    final var adapterToUse = (value instanceof ArrayList) ?
                            (TypeAdapter<R>) gson.getDelegateAdapter(WrappedTypeAdapterFactory.this, TypeToken.get(value.getClass()))
                            : fieldTypeAdapter;

                    final var toString = adapterToUse.toJson(value);
                    final var writeObject = new JsonObject();
                    writeObject.addProperty(field.wrappedValue, toString);
                    // finally write this.
                    gson.toJson(writeObject, out);
                }
            }

            @Override
            public R read(JsonReader in) throws IOException {
                // first parse the json string to an object.
                final var next = in.nextString();
                final var asObjectWrapped = gson.fromJson(next, JsonObject.class);
                // next get the wrapped element as provided from the annotation.
                final var element = asObjectWrapped.get(field.wrappedValue);

                // if we have a JsonObject just read normally.
                if (element.isJsonObject()) return fieldTypeAdapter.fromJsonTree(element);

                // otherwise we can assume its an array.
                final var asArray = element.getAsJsonArray();
                // if we want to use raw types just return wrapped object.
                if (field.useRawType) return arrayTypeAdapter.fromJsonTree(asObjectWrapped);
                // finally we can create a new list from the R type and add each element to that list.
                // TODO: Figure out a way to just deserialize straight to an array rather than deserializing each element then adding it.
                final var list = new ArrayList<R>();
                asArray.forEach(jsonElement -> list.add(arrayTypeAdapter.fromJsonTree(jsonElement)));
                // cast and return.
                return (R) list;
            }
        };
    }

    /**
     * Represents one of the two annotations {@link WrappedArray} or {@link WrappedObject}
     */
    private static final class WrappedField {

        /**
         * The field type and the array type.
         */
        private final Class<?> fieldType, arrayType;
        /**
         * The wrapped value name.
         */
        private final String wrappedValue;
        /**
         * {@code true} to ignore returning a list and just returning the type.
         */
        private final boolean useRawType;

        private WrappedField(Class<?> fieldType, Class<?> arrayType, String wrappedValue, boolean useRawType) {
            this.fieldType = fieldType;
            this.arrayType = arrayType;
            this.wrappedValue = wrappedValue;
            this.useRawType = useRawType;
        }
    }

}
