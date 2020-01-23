package athena.events.resource.download.event;

import athena.events.resource.download.player.EventPlayer;
import athena.events.resource.download.template.EventTemplate;
import athena.events.resource.download.window.FortniteEventWindow;
import athena.events.service.EventsPublicService;

import java.util.List;

/**
 * Represents a response from the {@link EventsPublicService} download endpoint.
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
     * List of event templates.
     */
    private List<EventTemplate> templates;

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
     * @return the list of templates.
     */
    public List<EventTemplate> templates() {
        return templates;
    }

    /**
     * Get an event by ID.
     *
     * @param eventId the event ID.
     * @return the {@link FortniteEvent} if found or {@code null}
     */
    public FortniteEvent getByEventId(String eventId) {
        return events
                .stream()
                .filter(event -> event.eventId().equals(eventId))
                .findAny()
                .orElse(null);
    }

    /**
     * Get an event by window ID.
     *
     * @param windowId the window ID.
     * @return the {@link FortniteEvent} if found or {@code null}
     */
    public FortniteEvent getEventByWindowId(String windowId) {
        return events
                .stream()
                .filter(event -> event.eventWindows().stream().anyMatch(fortniteEventWindow -> fortniteEventWindow.eventWindowId().equals(windowId)))
                .findAny()
                .orElse(null);
    }

    public FortniteEvent getByDisplayDataId(String displayDataId) {
        return events
                .stream()
                .filter(event -> event.displayDataId().equalsIgnoreCase(displayDataId))
                .findAny()
                .orElse(null);
    }

    /**
     * Get an {@link FortniteEventWindow} by window ID.
     *
     * @param windowId the window ID.
     * @return the {@link FortniteEventWindow} if found or {@code null}
     */
    public FortniteEventWindow getEventWindow(String windowId) {
        final var matching = getEventByWindowId(windowId);
        if (matching == null) return null;
        return matching
                .eventWindows()
                .stream()
                .filter(event -> event.eventWindowId().equals(windowId))
                .findAny()
                .orElse(null);
    }

    /**
     * Get a {@link EventTemplate} by the ID.
     *
     * @param eventTemplateId the event template ID.
     * @return the {@link EventTemplate} if found or {@code null}
     */
    public EventTemplate getTemplateById(String eventTemplateId) {
        return templates
                .stream()
                .filter(eventTemplate -> eventTemplate.eventTemplateId().equalsIgnoreCase(eventTemplateId))
                .findAny()
                .orElse(null);
    }

}
