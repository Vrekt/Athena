package athena.events.resource.leaderboard.entry;

import athena.events.resource.leaderboard.EventLeaderboard;
import athena.events.resource.leaderboard.session.EventSession;

import java.util.List;
import java.util.Map;

/**
 * Represents a single entry within {@link EventLeaderboard}
 */
public final class EventLeaderboardEntry {

    /**
     * "Fortnite",
     * "epicgames_S11_WinterRoyale_PC_NAE",
     * "S11_WinterRoyale_PC_NAE_Event1",
     * session ID,
     * "accountId1:accountId2:etc:etc"
     */
    private String gameId, eventId, eventWindowId, liveSessionId, teamId;
    /**
     * team account IDS, and tokens.
     * ex: GroupIdentity_GeoIdentity_unitedstates
     */
    private List<String> teamAccountIds, tokens;
    /**
     * Points earned and rank.
     */
    private int pointsEarned, rank;
    /**
     * Score.
     */
    private long score;
    /**
     * Percentile.
     */
    private double percentile;
    /**
     * point breakdown for this entry.
     * {TEAM_ELIMS_STAT_INDEX:1={timesAchieved=11, pointsEarned=71}, PLACEMENT_STAT_INDEX:1={timesAchieved=5, pointsEarned=75}}
     */
    private Map<String, Map<String, Integer>> pointBreakdown;
    /**
     * Session history for this entry.
     */
    private List<EventSession> sessionHistory;

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
     * @return the live session ID.
     */
    public String liveSessionId() {
        return liveSessionId;
    }

    /**
     * @return the team ID.
     */
    public String teamId() {
        return teamId;
    }

    /**
     * @return list of team account IDs.
     */
    public List<String> teamAccountIds() {
        return teamAccountIds;
    }

    /**
     * @return a list of tokens
     */
    public List<String> tokens() {
        return tokens;
    }

    /**
     * @return points earned for this entry.
     */
    public int pointsEarned() {
        return pointsEarned;
    }

    /**
     * @return the rank
     */
    public int rank() {
        return rank;
    }

    /**
     * @return the score
     */
    public long score() {
        return score;
    }

    /**
     * @return the percentile
     */
    public double percentile() {
        return percentile;
    }

    /**
     * @return point breakdown for this entry.
     */
    public Map<String, Map<String, Integer>> pointBreakdown() {
        return pointBreakdown;
    }

    /**
     * @return session history for this entry.
     */
    public List<EventSession> sessionHistory() {
        return sessionHistory;
    }
}
