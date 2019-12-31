package athena.events.resource.download.event;

import athena.events.resource.download.player.EventPlayer;
import athena.events.resource.download.window.FortniteEventWindow;

import java.util.List;
import java.util.Optional;

/**
 * Represents a response from the {@link athena.events.EventsPublicService} download endpoint.
 */
public final class FortniteEventDownload {

    /**
     * The player for this event download.
     */
    private EventPlayer player;

    /**
     * A list of events.
     */
    private List<FortniteEvent> events;

    /**
     * @return The player for this event download.
     */
    public EventPlayer player() {
        return player;
    }

    /**
     * @return the list of events.
     */
    public List<FortniteEvent> events() {
        return events;
    }

    /**
     * Get an event by ID.
     *
     * @param eventId the event ID.
     * @return a {@link Optional} containing the event if found.
     */
    public Optional<FortniteEvent> getByEventId(String eventId) {
        return events.stream().filter(event -> event.eventId().equals(eventId)).findAny();
    }

    /**
     * Get an event by window ID.
     *
     * @param windowId the window ID.
     * @return a {@link Optional} containing the event if found.
     */
    public Optional<FortniteEvent> getEventByWindowId(String windowId) {
        return events.stream().filter(event -> event.eventWindows().stream().anyMatch(fortniteEventWindow -> fortniteEventWindow.eventWindowId().equals(windowId))).findAny();
    }

    /**
     * Get an {@link FortniteEventWindow} by window ID>
     *
     * @param windowId the window ID.
     * @return a {@link Optional} containing the {@link FortniteEventWindow} if found.
     */
    public Optional<FortniteEventWindow> getEventWindow(String windowId) {
        final var matching = getEventByWindowId(windowId);
        if (matching.isEmpty()) return Optional.empty();
        return matching.get().eventWindows().stream().filter(event -> event.eventWindowId().equals(windowId)).findAny();
    }

}
