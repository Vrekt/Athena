package athena.events.resource.leaderboard.session;

import athena.events.resource.leaderboard.statistic.LeaderboardStatistic;
import athena.events.resource.leaderboard.entry.EventLeaderboardEntry;

import java.time.Instant;
import java.util.Map;

/**
 * Represents an event session for a {@link EventLeaderboardEntry}
 */
public final class EventSession {

    /**
     * The session ID for this session.
     */
    private String sessionId;
    /**
     * When this session ended.
     */
    private Instant endTime;

    /**
     * A map of tracked-stats.
     */
    private Map<LeaderboardStatistic, Integer> trackedStats;

    /**
     * @return the session ID.
     */
    public String sessionId() {
        return sessionId;
    }

    /**
     * @return when this session ended.
     */
    public Instant endTime() {
        return endTime;
    }

    /**
     * @return the tracked stats for this session.
     */
    public Map<LeaderboardStatistic, Integer> trackedStats() {
        return trackedStats;
    }
}
