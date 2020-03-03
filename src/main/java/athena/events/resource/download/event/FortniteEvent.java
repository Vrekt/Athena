package athena.events.resource.download.event;

import athena.events.resource.download.window.FortniteEventWindow;
import athena.types.Platform;
import athena.types.Region;

import java.time.Instant;
import java.util.List;
import java.util.Map;

/**
 * Represents a fortnite event.
 */
public final class FortniteEvent {

    /**
     * "epicgames_Arena_S11_Duos",
     * "arena_duos"
     * ???
     * ???
     * ???
     */
    private String eventId, displayDataId, appId, environment, metadata;
    /**
     * Announcement time of this event.
     */
    private Instant announcementTime;
    /**
     * List of regions this event is in.
     */
    private List<Region> regions;
    /**
     * List of platforms this event supports.
     */
    private List<Platform> platforms;
    /**
     * Region mappings,
     * "NAE": "NAECOMP",
     * "NAW": "NAECOMP"
     */
    private Map<String, String> regionMappings;
    /**
     * A list of event windows for this event.
     */
    private List<FortniteEventWindow> eventWindows;

    /**
     * @return the event ID.
     */
    public String eventId() {
        return eventId;
    }

    /**
     * @return the display data ID. eg: "epicgames_duos"
     */
    public String displayDataId() {
        return displayDataId;
    }

    /**
     * @return usually null.
     */
    public String appId() {
        return appId;
    }

    /**
     * @return usually null.
     */
    public String environment() {
        return environment;
    }

    /**
     * @return usually null.
     */
    public String metadata() {
        return metadata;
    }

    /**
     * @return the announcement time of this event.
     */
    public Instant announcementTime() {
        return announcementTime;
    }

    /**
     * @return regions supported by this event?
     */
    public List<Region> regions() {
        return regions;
    }

    /**
     * @return platforms supported by this event?
     */
    public List<Platform> platforms() {
        return platforms;
    }

    /**
     * @return Region mappings,
     * * "NAE": "NAECOMP",
     * * "NAW": "NAECOMP"
     */
    public Map<String, String> regionMappings() {
        return regionMappings;
    }

    /**
     * @return list of event windows for this event.
     */
    public List<FortniteEventWindow> eventWindows() {
        return eventWindows;
    }

    /**
     * @param platform the platform
     * @return {@code true} if this event supports the provided {@code platform}
     */
    public boolean isPlatformSupported(Platform platform) {
        return platforms.stream().anyMatch(p -> p == platform);
    }

    /**
     * @param platform the platform (case sensitive)
     * @return {@code true} if this event supports the provided {@code platform}
     */
    public boolean isPlatformSupported(String platform) {
        return platforms.stream().anyMatch(p -> p.names().contains(platform));
    }

    /**
     * @param region the region
     * @return {@code true} if this event supports the provided {@code region}
     */
    public boolean isRegionSupported(Region region) {
        return regions.stream().anyMatch(r -> r == region);
    }

    /**
     * @param region the region (case in-sensitive)
     * @return {@code true} if this event supports the provided {@code region}
     */
    public boolean isRegionSupported(String region) {
        return regions.stream().anyMatch(r -> r.name().equalsIgnoreCase(region));
    }

    /**
     * Checks if the token is required for the provided {@code eventWindow}
     *
     * @param tokenName   the token name, ex: "ARENA_S11_Division8"
     * @param eventWindow the event window
     * @return {@code true} if the token is required.
     */
    public boolean doesRequireToken(String tokenName, FortniteEventWindow eventWindow) {
        if (eventWindow.requireAllTokens().contains(tokenName)) return true;
        if (eventWindow.requireAnyTokens().contains(tokenName)) return true;
        return !eventWindow.requireNoneTokensCaller().contains(tokenName);
    }

    /**
     * Checks if the token is required for the provided {@code eventWindowId}
     *
     * @param tokenName     the token name, ex: "ARENA_S11_Division8"
     * @param eventWindowId the event window
     * @return {@code true} if the token is required.
     */
    public boolean doesRequireToken(String tokenName, String eventWindowId) {
        final var eventWindow = eventWindows.stream().filter(window -> window.eventWindowId().equalsIgnoreCase(eventWindowId)).findAny().orElseThrow();
        return doesRequireToken(tokenName, eventWindow);
    }

}
