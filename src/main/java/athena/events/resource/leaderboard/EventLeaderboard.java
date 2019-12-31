package athena.events.resource.leaderboard;

import athena.events.resource.download.event.FortniteEvent;
import athena.events.resource.leaderboard.entry.EventLeaderboardEntry;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Represents a leaderboard for a {@link FortniteEvent}
 * TODO: Live sessions still.
 */
public final class EventLeaderboard {

    /**
     * "Fortnite",
     * "epicgames_S11_WinterRoyale_PC_NAE",
     * "S11_WinterRoyale_PC_NAE_Event1"
     */
    private String gameId, eventId, eventWindowId;
    /**
     * Current page and the total number of pages.
     */
    private int page, totalPages;
    /**
     * When this leaderboard was last updated?
     */
    private Instant updatedTime;
    /**
     * All leaderboard entries.
     */
    private List<EventLeaderboardEntry> entries;

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
     * @return the event window ID.
     */
    public String eventWindowId() {
        return eventWindowId;
    }

    /**
     * @return the current page.
     */
    public int page() {
        return page;
    }

    /**
     * @return the total number of pages.
     */
    public int totalPages() {
        return totalPages;
    }

    /**
     * @return when this leaderboard was last updated?
     */
    public Instant updatedTime() {
        return updatedTime;
    }

    /**
     * @return all leaderboard entries.
     */
    public List<EventLeaderboardEntry> entries() {
        return entries;
    }

    /**
     * Get a {@link EventLeaderboardEntry} by account ID.
     *
     * @param accountId the account ID within the team.
     * @return a {@link Optional} containing the {@link EventLeaderboardEntry} if found.
     */
    public Optional<EventLeaderboardEntry> getEntryByAccountId(String accountId) {
        return entries.stream().filter(entry -> entry.teamAccountIds().contains(accountId)).findAny();
    }

    /**
     * Get a {@link EventLeaderboardEntry} by team ID.
     *
     * @param teamId the team ID.
     * @return a {@link Optional} containing the {@link EventLeaderboardEntry} if found.
     */
    public Optional<EventLeaderboardEntry> getEntryByTeamId(String teamId) {
        return entries.stream().filter(entry -> entry.teamId().equals(teamId)).findAny();
    }

    /**
     * Get a list of {@link EventLeaderboardEntry} containing the {@code sessionId}
     *
     * @param sessionId the session ID
     * @return a list of {@link EventLeaderboardEntry}
     */
    public List<EventLeaderboardEntry> getEntriesBySessionId(String sessionId) {
        return entries.stream().filter(entry -> entry.sessionHistory().stream().anyMatch(eventSession -> eventSession.sessionId().equals(sessionId))).collect(Collectors.toList());
    }

}
