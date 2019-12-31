package athena.events;

import athena.events.resource.download.event.FortniteEventDownload;
import athena.events.resource.leaderboard.EventLeaderboard;
import athena.util.request.Requests;

/**
 * Provides easy access to the {@link EventsPublicService}
 */
public final class Events {

    /**
     * Current authenticated account ID.
     */
    private final String localAccountId;
    /**
     * Service for requests.
     */
    private final EventsPublicService service;

    public Events(EventsPublicService eventsPublicService, String localAccountId) {
        this.service = eventsPublicService;
        this.localAccountId = localAccountId;
    }

    /**
     * Download events for this account.
     *
     * @param region   the region, ex: NAE
     * @param platform the platform, ex: Windows for platforms see {@link athena.types.Platform}
     * @return a {@link FortniteEventDownload} representing the response.
     */
    public FortniteEventDownload downloadEvents(String region, String platform) {
        return downloadEvents(region, platform, localAccountId);
    }

    /**
     * Download events for this account/team.
     *
     * @param region         the region, ex: NAE
     * @param platform       the platform, ex: Windows for platforms see {@link athena.types.Platform}
     * @param teamAccountIds the account IDs of the team must be separated by comma. {accountId1,accountId2,etc}
     * @return a {@link FortniteEventDownload} representing the response.
     */
    public FortniteEventDownload downloadEvents(String region, String platform, String teamAccountIds) {
        final var call = service.download(localAccountId, region, platform, teamAccountIds);
        return Requests.executeCall("Failed to download events for " + localAccountId + ", region: " + region + ", platform: " + platform + ", team: " + teamAccountIds, call);
    }

    /**
     * Download event data for this account.
     *
     * @param region         the region, ex: NAE
     * @param showPastEvents {@code true} if past events should be given.
     * @return a {@link FortniteEventDownload} representing the response.
     */
    public FortniteEventDownload downloadData(String region, boolean showPastEvents) {
        final var call = service.data(localAccountId, region, showPastEvents);
        return Requests.executeCall("Failed to download data for " + localAccountId + ", region: " + region + ", showPastEvents: " + showPastEvents, call);
    }

    /**
     * Retrieve leaderboards for an event.
     *
     * @param eventId          the event ID.
     * @param eventWindowId    the event window ID.
     * @param page             the page, or {@code 0}
     * @param rank             the rank, or {@code 0}
     * @param showLiveSessions {@code true} if live sessions should be shown, TODO: Not supported yet.
     * @return a {@link EventLeaderboard} representing the response.
     */
    public EventLeaderboard leaderboards(String eventId, String eventWindowId, int page, int rank, boolean showLiveSessions) {
        return leaderboards(eventId, eventWindowId, localAccountId, page, rank, showLiveSessions);
    }

    /**
     * Retrieve leaderboards for an event.
     *
     * @param eventId          the event ID.
     * @param eventWindowId    the event window ID.
     * @param teamAccountIds   the account IDs of the team must be separated by comma. {accountId1,accountId2,etc}
     * @param page             the page, or {@code 0}
     * @param rank             the rank, or {@code 0}
     * @param showLiveSessions {@code true} if live sessions should be shown, TODO: Not supported yet.
     * @return a {@link EventLeaderboard} representing the response.
     */
    public EventLeaderboard leaderboards(String eventId, String eventWindowId, String teamAccountIds, int page, int rank, boolean showLiveSessions) {
        final var call = service.leaderboards(eventId, eventWindowId, localAccountId, page, rank, teamAccountIds, "Fortnite", showLiveSessions);
        return Requests.executeCall("Failed to get leaderboard for " + eventId + ", window: " + eventWindowId + ", page: " + page + ", rank: " + rank, call);
    }

}
