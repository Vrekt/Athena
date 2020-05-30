package athena.stats;

import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.stats.resource.UnfilteredStatistic;
import athena.stats.resource.leaderboard.LeaderboardResponse;
import athena.stats.resource.query.IndividualQueryResponse;
import athena.stats.resource.query.StatisticsQuery;
import athena.stats.service.StatsproxyPublicService;
import athena.util.request.Requests;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Provides easy access to {@link StatsproxyPublicService}
 */
public final class StatisticsV2 {

    /**
     * The service that handles the requests.
     */
    private final StatsproxyPublicService service;

    /**
     * The accounts service.
     */
    private final AccountPublicService accountPublicService;

    public StatisticsV2(StatsproxyPublicService service, AccountPublicService accountPublicService) {
        this.service = service;
        this.accountPublicService = accountPublicService;
    }

    /**
     * Retrieves the statistics for the provided account.
     *
     * @param accountId the ID of the account
     * @return a {@link UnfilteredStatistic} object
     */
    public UnfilteredStatistic stats(String accountId) {
        final var call = service.stats(accountId);
        return Requests.executeCall(call);
    }

    /**
     * Query specific stats for some accounts.
     *
     * @param query the query
     * @return a {@link List} that will contain each query result.
     */
    public List<IndividualQueryResponse> query(StatisticsQuery query) {
        final var call = service.query(query);
        return Requests.executeCall(call);
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
        return Requests.executeCall(call);
    }

    /**
     * Retrieve the leaderboard for a specific statistic, ex: br_placetop1_keyboardmouse_m0_playlist_defaultsolo
     *
     * @param type the statistic type, ex: 'br_placetop1_keyboardmouse_m0_playlist_defaultsolo"
     * @return a {@link Map} that contains entries by account ID and their value.
     */
    public LeaderboardResponse leaderboard(String type) {
        final var call = service.leaderboard(type);
        return Requests.executeCall(call);
    }

    /**
     * Retrieve the leaderboard for a specific statistic, ex: br_placetop1_keyboardmouse_m0_playlist_defaultsolo
     * This method converts each account ID to an account, which takes longer.
     *
     * @param type the statistic type, ex: 'br_placetop1_keyboardmouse_m0_playlist_defaultsolo"
     * @return a {@link LinkedHashMap} (that is sorted) that contains entries by account and their value.
     */
    public Map<Account, Integer> leaderboardWithAccounts(String type) {
        // leaderboard response
        final var response = leaderboard(type);
        final var entriesMap = response.mapOfEntries();
        final var accountIds = new ArrayList<>(entriesMap.keySet());

        // the final map to return.
        final var map = new LinkedHashMap<Account, Integer>();
        // split account IDs into lists of 100 for bulk find.
        final var partitioned = Lists.partition(accountIds, 100);
        partitioned.forEach(list -> {
            // find and execute then put in map.
            final var call = accountPublicService.findManyByAccountId(list.toArray(String[]::new));
            final var result = Requests.executeCall(call);
            result.forEach(account -> map.put(account, entriesMap.get(account.accountId())));
        });

        // finally sort and then return.
        return map.entrySet().stream().sorted((Map.Entry.<Account, Integer>comparingByValue().reversed())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

}
