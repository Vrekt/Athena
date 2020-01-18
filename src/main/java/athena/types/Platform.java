package athena.types;

import java.util.List;

/**
 * Represents all the platforms.
 */
public enum Platform {
    WIN("WIN", "Windows"),
    MAC("MAC", "Mac"),
    IOS("IOS"),
    AND("AND", "Android"),
    PSN("PS4", "PSN"),
    XBL("XBL", "XboxOne", "XUID", "XB1"),
    SWT("SWT", "Switch", "Nintendo"),
    NONE("NONE");

    private static final List<Platform> TYPES = List.of(values());

    private final List<String> names;

    Platform(String... names) {
        this.names = List.of(names);
    }

    /**
     * Attempt to get the platform types the {@code platform} is referencing.
     *
     * @param platform the platform
     * @return {@link Platform} or {@code null} if its an invalid platform.
     */
    public static Platform typeOf(String platform) {
        if (platform == null) return null;
        return TYPES.stream().filter(type -> type.name().equalsIgnoreCase(platform) || type.names.stream().anyMatch(str -> str.equalsIgnoreCase(platform))).findAny().orElse(NONE);
    }

    /**
     * @return first name in the list.
     */
    public String primaryName() {
        return names.get(0);
    }

    /**
     * @return all names.
     */
    public List<String> names() {
        return names;
    }

}
