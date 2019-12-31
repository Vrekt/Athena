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
     * "Fortnite",
     * "epicgames_Arena_S11_Duos",
     * "arena_duos"
     */
    private String gameId, eventId, displayDataId;
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
     * @return "Fortnite"
     */
    public String gameId() {
        return gameId;
    }

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

}
