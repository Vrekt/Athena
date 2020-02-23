package athena.types;

import java.util.List;

/**
 * Represents all the input types.
 */
public enum Input {

    KEYBOARD_AND_MOUSE("MouseAndKeyboard", "KeyboardMouse"), TOUCH("Touch"), GAMEPAD("Gamepad");

    private static final List<Input> TYPES = List.of(values());
    private final List<String> names;

    Input(String... names) {
        this.names = List.of(names);
    }

    public List<String> getNames() {
        return names;
    }

    /**
     * Gets the {@link Input} from the provided {@code key} name.
     *
     * @param key the name of the input
     * @return a {@link Input} or {@code null} if not found.
     */
    public static Input typeOf(String key) {
        if (key == null) return null;
        return TYPES.stream().filter(type -> type.names.stream().anyMatch(key::equalsIgnoreCase)).findAny().orElse(null);
    }
}
