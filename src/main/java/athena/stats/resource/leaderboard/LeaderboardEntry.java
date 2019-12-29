package athena.stats.resource.leaderboard;

/**
 * Represents a single leaderboard entry.
 */
public final class LeaderboardEntry {

    private final String accountId;
    private final int value;

    LeaderboardEntry(String accountId, int value) {
        this.accountId = accountId;
        this.value = value;
    }

    /**
     * @return the ID of the account.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the value.
     */
    public int value() {
        return value;
    }
}
