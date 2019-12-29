package athena.platform;

import java.util.List;

/**
 * Represents all the platforms.
 */
public enum Platform {
    WIN, MAC, IOS, AND, PSN, XBL, SWT;

    private static final List<Platform> TYPES = List.of(values());

    /**
     * Attempt to get the platform notification the {@code platform} is referencing.
     *
     * @param platform the platform
     * @return {@link Platform} or {@code null} if its an invalid platform.
     */
    public static Platform typeOf(String platform) {
        if (platform == null) return null;
        return TYPES.stream().filter(type -> type.name().equalsIgnoreCase(platform)).findAny().orElse(null);
    }

}
