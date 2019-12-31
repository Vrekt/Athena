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

    public String gameId() {
        return gameId;
    }

    public String eventId() {
        return eventId;
    }

    public String displayDataId() {
        return displayDataId;
    }

    public Instant announcementTime() {
        return announcementTime;
    }

    public List<Region> regions() {
        return regions;
    }

    public List<Platform> platforms() {
        return platforms;
    }

    public Map<String, String> regionMappings() {
        return regionMappings;
    }

    public List<FortniteEventWindow> eventWindows() {
        return eventWindows;
    }
}
