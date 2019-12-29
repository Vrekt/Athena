package athena.stats.resource.query;

import java.util.Map;

/**
 * Represents an individual query response.
 */
public final class IndividualQueryResponse {

    /**
     * Start and end times.
     */
    private long startTime, endTime;

    /**
     * A map of all statistics.
     */
    private Map<String, Long> stats;

    /**
     * The account ID of who these stats belong to.
     */
    private String accountId;

    /**
     * @return the start time, usually 0.
     */
    public long startTime() {
        return startTime;
    }

    /**
     * @return the end time usually a big ass number.
     */
    public long endTime() {
        return endTime;
    }

    /**
     * @return a map of all statistics by type, ex: Key: br_placetop1_keyboardmouse_m0_playlist_defaultsolo, Value: 111222333
     */
    public Map<String, Long> statistics() {
        return stats;
    }

    /**
     * @return the account ID of who these stats belong to.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * Get the value of the specified {@code stat}
     *
     * @param stat the type of value, eg: "br_placetop1_keyboardmouse_m0_playlist_defaultsolo"
     * @return the value represented as a long.
     */
    public Long getValue(String stat) {
        return stats.get(stat);
    }

}
