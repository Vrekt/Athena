package athena.stats.resource.leaderboard;

/**
 * Represents a single leaderboard entry.
 */
public final class LeaderboardEntry {

    /**
     * The account ID.
     */
    private String account;
    /**
     * The value.
     */
    private int value;

    /**
     * @return the ID of the account.
     */
    public String accountId() {
        return account;
    }

    /**
     * @return the value.
     */
    public int value() {
        return value;
    }
}
