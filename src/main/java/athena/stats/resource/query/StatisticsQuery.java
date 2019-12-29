package athena.stats.resource.query;

import org.apache.commons.lang3.ArrayUtils;

/**
 * A class used to build stats query.
 */
public final class StatisticsQuery {

    /**
     * App ID, should always be Fortnite
     */
    private final String appId = "Fortnite";

    /**
     * Array of statistics to query and an array of owners (account IDs)
     */
    private String[] stats, owners;

    /**
     * Start and end dates, can be left 0.
     */
    private long startDate, endDate;

    private StatisticsQuery(String[] stats, String[] accounts, long startDate, long endDate) {
        this.stats = stats;
        this.owners = accounts;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return the app ID.
     */
    public String appId() {
        return appId;
    }

    /**
     * @return an array of statistics.
     */
    public String[] statistics() {
        return stats;
    }

    /**
     * @return an array of accounts.
     */
    public String[] accounts() {
        return owners;
    }

    /**
     * @return the start date.
     */
    public long startDate() {
        return startDate;
    }

    /**
     * @return the end date.
     */
    public long endDate() {
        return endDate;
    }

    /**
     * Builder used to build this class.
     */
    public static class Builder {

        private String[] stats, owners;
        private long startDate, endDate;

        public Builder(String statistic, String account) {
            stats = ArrayUtils.add(null, statistic);
            owners = ArrayUtils.add(null, account);
        }

        public Builder(String statistic, String account, long startDate, long endDate) {
            this(statistic, account);
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Builder(String[] stats, String... accounts) {
            this.stats = stats;
            this.owners = accounts;
        }

        public Builder(String[] stats, String[] accounts, long startDate, long endDate) {
            this(stats, accounts);
            this.startDate = startDate;
            this.endDate = endDate;
        }

        public Builder addStatistics(String... statistics) {
            this.stats = ArrayUtils.addAll(stats, statistics);
            return this;
        }

        public Builder addAccounts(String... accounts) {
            this.owners = ArrayUtils.addAll(owners, accounts);
            return this;
        }

        public Builder setStartDate(long startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder setEndDate(long endDate) {
            this.endDate = endDate;
            return this;
        }

        public StatisticsQuery build() {
            return new StatisticsQuery(stats, owners, startDate, endDate);
        }
    }
}
