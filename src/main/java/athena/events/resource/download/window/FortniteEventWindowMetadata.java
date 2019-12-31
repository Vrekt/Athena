package athena.events.resource.download.window;

import com.google.gson.annotations.SerializedName;

/**
 * Represents metadata of a {@link FortniteEventWindow}
 */
public final class FortniteEventWindowMetadata {

    /**
     * The round type of this event window:
     * "Arena"
     * "Finals"
     * "Qualifiers"
     * "SemiFinals"
     * etc.
     */
    @SerializedName("RoundType")
    private String roundType;
    /**
     * The threshold to advance to the next division?
     */
    @SerializedName("ThresholdToAdvanceDivision")
    private int thresholdToAdvanceDivision;
    /**
     * Division rank required?
     */
    private int divisionRank;
    /**
     * {@code true} if server replays are enabled?
     */
    @SerializedName("ServerReplays")
    private boolean serverReplays;
    /**
     * Matchmaking delay seconds.
     */
    @SerializedName("ScheduledMatchmakingMatchDelaySeconds")
    private int scheduledMatchmakingDelaySeconds;
    /**
     * Initial matchmaking delay seconds.
     */
    @SerializedName("ScheduledMatchmakingInitialDelaySeconds")
    private int scheduledMatchmakingInitialDelaySeconds;

    /**
     * @return the round type
     */
    String roundType() {
        return roundType;
    }

    /**
     * @return threshold to advance to the next division?
     */
    int thresholdToAdvanceDivision() {
        return thresholdToAdvanceDivision;
    }

    /**
     * @return division rank required?
     */
    int divisionRank() {
        return divisionRank;
    }

    /**
     * @return {@code true} if server replays are enabled?
     */
    boolean serverReplays() {
        return serverReplays;
    }

    /**
     * @return matchmaking delay.
     */
    int scheduledMatchmakingDelaySeconds() {
        return scheduledMatchmakingDelaySeconds;
    }

    /**
     * @return initial matchmaking delay.
     */
    int scheduledMatchmakingInitialDelaySeconds() {
        return scheduledMatchmakingInitialDelaySeconds;
    }
}
