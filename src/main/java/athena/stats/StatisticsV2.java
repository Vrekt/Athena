package athena.stats;

import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.stats.resource.UnfilteredStatistic;
import athena.stats.resource.leaderboard.LeaderboardEntry;
import athena.stats.resource.leaderboard.LeaderboardResponse;
import athena.stats.resource.query.IndividualQueryResponse;
import athena.stats.resource.query.StatisticsQuery;
import athena.stats.service.StatsproxyPublicService;
import athena.util.request.Requests;
import com.google.common.collect.Lists;
import com.google.common.flogger.FluentLogger;
import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides easy access to {@link StatsproxyPublicService}
 */
public final class StatisticsV2 {

    /**
     * The LOGGER
     */
    private static final FluentLogger LOGGER = FluentLogger.forEnclosingClass();

    /**
     * The service that handles the requests.
     */
    private final StatsproxyPublicService service;

    /**
     * The accounts service.
     */
    private final AccountPublicService accountPublicService;

    public StatisticsV2(StatsproxyPublicService statsproxyPublicService, AccountPublicService accountPublicService) {
        this.service = statsproxyPublicService;
        this.accountPublicService = accountPublicService;
    }

    /**
     * Retrieves the statistics for the provided account.
     *
     * @param accountId the ID of the account
     * @return a {@link UnfilteredStatistic} object
     */
    public UnfilteredStatistic retrieveStatsFor(String accountId) {
        final var call = service.stats(accountId);
        return Requests.executeCall("Failed to get stats for " + accountId, call);
    }

    /**
     * Query specific stats for some accounts.
     *
     * @param query the query
     * @return a {@link List} that will contain each query result.
     */
    public List<IndividualQueryResponse> query(StatisticsQuery query) {
        final var call = service.query(query);
        return Requests.executeCallOptional("Failed to query stats for account(s) " + Arrays.toString(query.accounts()), call).orElse(List.of());
    }

    /**
     * Query one statistic for one account.
     *
     * @param statistic the statistic
     * @param accountId the account
     * @return a {@link IndividualQueryResponse} representing the response.
     */
    public IndividualQueryResponse queryOne(String statistic, String accountId) {
        return query(new StatisticsQuery.Builder(statistic, accountId).build()).get(0);
    }

    /**
     * Query specific stats for some accounts.
     *
     * @param stats    the statistic types
     * @param accounts the accounts
     * @return a {@link List} that will contain each query result.
     */
    public List<IndividualQueryResponse> query(String[] stats, String... accounts) {
        final var call = service.query(new StatisticsQuery.Builder(stats, accounts).build());
        return Requests.executeCallOptional("Failed to query stats for account(s) " + ArrayUtils.toString(accounts), call).orElse(List.of());
    }

    /**
     * Retrieve the leaderboard for a specific statistic, ex: br_placetop1_keyboardmouse_m0_playlist_defaultsolo
     *
     * @param type the statistic type, ex: 'br_placetop1_keyboardmouse_m0_playlist_defaultsolo"
     * @return a {@link Map} that contains entries by account ID and their value.
     */
    public LeaderboardResponse retrieveLeaderboardFor(String type) {
        final var call = service.leaderboard(type);
        return Requests.executeCall("Failed to retrieve leaderboard for " + type, call);
    }

    /**
     * Retrieve the leaderboard for a specific statistic, ex: br_placetop1_keyboardmouse_m0_playlist_defaultsolo
     * This method converts each account ID to an account, which takes longer.
     *
     * @param type the statistic type, ex: 'br_placetop1_keyboardmouse_m0_playlist_defaultsolo"
     * @return a {@link LinkedHashMap} (that is sorted) that contains entries by account and their value.
     */
    public Map<Account, Integer> retrieveLeaderboardAndGetAccountsFor(String type) {
        final var response = retrieveLeaderboardFor(type);
        final var values = response.values();
        final var entries = response.asEntries();
        final var map = new HashMap<Account, Integer>();
        final var partitioned = Lists.partition(entries, 100);

        // go through each partitioned list.
        partitioned.forEach(list -> {
            try {
                // map them to account ID.
                final var as = list.stream().map(LeaderboardEntry::accountId).collect(Collectors.toList());
                // find each account.
                final var accounts = accountPublicService.findManyByAccountId(as.toArray(String[]::new)).execute().body();
                if (accounts == null) {
                    // failed
                    LOGGER.atSevere().log("Failed to retrieve leaderboard entries for " + type + " and convert results to accounts.");
                } else {
                    accounts.forEach(account -> map.put(account, values.get(account.accountId())));
                }
            } catch (final IOException exception) {
                LOGGER.atSevere().withCause(exception).log("Failed to retrieve leaderboard entries for " + type + " and convert results to accounts.");
            }
        });
        // finally re-order the elements and return;
        return map.entrySet().stream().sorted((Map.Entry.<Account, Integer>comparingByValue().reversed())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
