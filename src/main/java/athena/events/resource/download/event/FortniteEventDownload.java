package athena.events.resource.download.event;

import athena.events.resource.download.player.EventPlayer;
import athena.events.resource.download.template.EventTemplate;
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

    /**
     * Get a {@link EventTemplate} by the ID.
     *
     * @param eventTemplateId the event template ID.
     * @return a {@link Optional} containing the {@link EventTemplate} if found.
     */
    public Optional<EventTemplate> getTemplateById(String eventTemplateId) {
        return templates.stream().filter(eventTemplate -> eventTemplate.eventTemplateId().equalsIgnoreCase(eventTemplateId)).findAny();
    }

}
