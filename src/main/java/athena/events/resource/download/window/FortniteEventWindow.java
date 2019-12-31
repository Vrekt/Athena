package athena.events.resource.download.window;

import java.time.Instant;
import java.util.List;

/**
 * Represents a fortnite event window.
 */
public final class FortniteEventWindow {

    /**
     * "S11_FNCS_Finals_NAE_Event1",
     * "EventData_S11_FNCS_Finals_NAE_Heats",
     * "Fortnite_NA",
     * "locked" or "public"
     * "all" or "any"
     */
    private String eventWindowId, eventTemplateId, leaderboardId, visibility, teammateEligibility;

    /**
     * Various times for this event window.
     */
    private Instant countdownBeginTime, beginTime, endTime;

    /**
     * Round and payout-delay.
     */
    private int round, payoutDelay;

    /**
     * {@code true} if this event is "to be determined"
     */
    private boolean isTBD;

    /**
     * The metadata for this event window.
     */
    private FortniteEventWindowMetadata metadata;

    /**
     * Various tokens/callers.
     */
    private List<String> requireAllTokens, requireAnyTokens, requireNoneTokensCaller, requireAllTokensCaller, requireAnyTokensCaller, additionalRequirements;

    /**
     * @return the event window ID, ex: S11_FNCS_Week1_NAE_Event3
     */
    public String eventWindowId() {
        return eventWindowId;
    }

    /**
     * @return the event template ID, ex: EventData_S11_FNCS_Finals_NAE_Heats
     */
    public String eventTemplateId() {
        return eventTemplateId;
    }

    /**
     * @return the leaderboard ID, ex: Fortnite_NA
     */
    public String leaderboardId() {
        return leaderboardId;
    }

    /**
     * @return the visibly of this event, "locked" or "public"
     */
    public String visibility() {
        return visibility;
    }

    /**
     * @return {@code "all"} or {@code "any"}
     */
    public String teammateEligibility() {
        return teammateEligibility;
    }

    /**
     * @return when the countdown begins.
     */
    public Instant countdownBeginTime() {
        return countdownBeginTime;
    }

    /**
     * @return when this event window starts.
     */
    public Instant beginTime() {
        return beginTime;
    }

    /**
     * @return when this event window ends.
     */
    public Instant endTime() {
        return endTime;
    }

    /**
     * @return the round of this event window.
     */
    public int round() {
        return round;
    }

    /**
     * @return the payout-delay.
     */
    public int payoutDelay() {
        return payoutDelay;
    }

    /**
     * @return {@code true} if this event window is "to be determined"
     */
    public boolean isTBD() {
        return isTBD;
    }

    /**
     * @return the round type
     */
    public String roundType() {
        return metadata.roundType();
    }

    /**
     * @return threshold to advance to the next division?
     */
    public int thresholdToAdvanceDivision() {
        return metadata.thresholdToAdvanceDivision();
    }

    /**
     * @return division rank required?
     */
    public int divisionRank() {
        return metadata.divisionRank();
    }

    /**
     * @return {@code true} if server replays are enabled?
     */
    public boolean serverReplays() {
        return metadata.serverReplays();
    }

    /**
     * @return matchmaking delay.
     */
    public int scheduledMatchmakingDelaySeconds() {
        return metadata.scheduledMatchmakingDelaySeconds();
    }

    /**
     * @return initial matchmaking delay.
     */
    public int scheduledMatchmakingInitialDelaySeconds() {
        return metadata.scheduledMatchmakingInitialDelaySeconds();
    }

    /**
     * @return tokens required?
     */
    public List<String> requireAllTokens() {
        return requireAllTokens;
    }

    /**
     * @return any of these tokens required?
     */
    public List<String> requireAnyTokens() {
        return requireAnyTokens;
    }

    /**
     * @return require nothing if one of the token callers is present?
     */
    public List<String> requireNoneTokensCaller() {
        return requireNoneTokensCaller;
    }

    /**
     * @return require all if one of the token callers is present?
     */
    public List<String> requireAllTokensCaller() {
        return requireAllTokensCaller;
    }

    /**
     * @return require any if any token caller is present?
     */
    public List<String> requireAnyTokensCaller() {
        return requireAnyTokensCaller;
    }

    /**
     * @return other requirements, such as "mfa" and EULA, ex: eula:s11_cc_contenders_solo
     */
    public List<String> additionalRequirements() {
        return additionalRequirements;
    }
}
