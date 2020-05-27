package athena.util.json.wrapped;

import athena.util.json.wrapped.annotation.WrappedArray;
import athena.util.json.wrapped.annotation.WrappedObject;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

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
    private final Map<Type, AnnotatedField> fields = new HashMap<>();

    /**
     * Initialize a new instance.
     *
     * @param baseType the type
     * @return a new {@link WrappedTypeAdapterFactory}
     */
    public static WrappedTypeAdapterFactory of(Class<?> baseType) {
        return new WrappedTypeAdapterFactory(baseType);
    }

    /**
     * Initialize
     *
     * @param baseType the base type class.
     */
    private WrappedTypeAdapterFactory(Class<?> baseType) {
        for (var field : baseType.getDeclaredFields()) {
            final var objectAnnotation = field.getAnnotation(WrappedObject.class);
            final var arrayAnnotation = field.getAnnotation(WrappedArray.class);

            if (objectAnnotation != null) {
                // we have a field with the wrapped object annotation.
                fields.put(field.getType(), new AnnotatedField(field.getType(), null, objectAnnotation.value()));
            } else if (arrayAnnotation != null) {
                // we have a field with the wrapped array annotation.
                // if we have no list use the default type, otherwise get a parameterized List type of the type
                final var arrayType = arrayAnnotation.isNotList() ? arrayAnnotation.type() : TypeToken.getParameterized(List.class, arrayAnnotation.type()).getType();
                fields.put(arrayType, new AnnotatedField(arrayAnnotation.type(), arrayAnnotation.type(), arrayAnnotation.value(), arrayAnnotation.isConstant()));
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public <R> TypeAdapter<R> create(Gson gson, TypeToken<R> type) {
        if (!fields.containsKey(type.getType())) return null;

        // retrieve our field
        final var field = fields.get(type.getType());
        return new TypeAdapter<>() {

            // our adapter to use
            final TypeAdapter<R> fieldTypeAdapter = (TypeAdapter<R>) gson.getDelegateAdapter(WrappedTypeAdapterFactory.this, TypeToken.get(field.fieldType));

            @Override
            public void write(JsonWriter out, R value) throws IOException {
                // we have no value so pass this onto the original delegate adapter.
                if (value == null) {
                    fieldTypeAdapter.write(out, null);
                    return;
                }

                if (field.arrayType == null) {
                    // we are writing an object.
                    final var object = new JsonObject();
                    object.add(field.wrappedValue, fieldTypeAdapter.toJsonTree(value));
                    out.value(object.toString());
                } else {

                    // find the adapter to use.
                    // if we are writing a list value then grab the delegate adapter for that.
                    // otherwise, use the field type adapter.
                    final var adapterToUse = (value instanceof ArrayList) ?
                            (TypeAdapter<R>) gson.getDelegateAdapter(WrappedTypeAdapterFactory.this, TypeToken.get(value.getClass()))
                            : fieldTypeAdapter;
                    final var object = new JsonObject();
                    if (field.isConstant) {
                        // WORK-AROUND:
                        // Since we have a constant value (JsonArray)
                        // just write it instead of deserializing with GSON.
                        object.add(field.wrappedValue, new JsonArray());
                    } else {
                        // otherwise.
                        object.add(field.wrappedValue, adapterToUse.toJsonTree(value));
                    }
                    out.value(object.toString());
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
                // WORK-AROUND:
                // If we have a constant JSON value (for example tile states and platform sessions)
                // just return it normally here.
                if (field.isConstant) return fieldTypeAdapter.fromJsonTree(asObjectWrapped);
                // otherwise we can assume its an array.
                final var asArray = element.getAsJsonArray();
                // if we want to use raw types just return wrapped object.
                // finally we can create a new list from the R type and add each element to that list.
                // TODO: Figure out a way to just deserialize straight to an array rather than deserializing each element then adding it.
                final var list = new ArrayList<R>();
                asArray.forEach(jsonElement -> list.add(fieldTypeAdapter.fromJsonTree(jsonElement)));
                // cast and return.
                return (R) list;
            }
        };
    }

    /**
     * Represents one of the two annotations {@link WrappedArray} or {@link WrappedObject}
     */
    private static final class AnnotatedField {

        /**
         * The field type and the array type.
         */
        private final Class<?> fieldType, arrayType;
        /**
         * The wrapped value name.
         */
        private final String wrappedValue;
        /**
         * {@code true} if this value is a constant array.
         */
        private final boolean isConstant;

        private AnnotatedField(Class<?> fieldType, Class<?> arrayType, String wrappedValue) {
            this(fieldType, arrayType, wrappedValue, false);
        }

        private AnnotatedField(Class<?> fieldType, Class<?> arrayType, String wrappedValue, boolean isConstant) {
            this.fieldType = fieldType;
            this.arrayType = arrayType;
            this.wrappedValue = wrappedValue;
            this.isConstant = isConstant;
        }
    }

}
