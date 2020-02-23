package athena.presence.resource;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.util.request.Requests;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the response from last-online in {@link athena.presence.service.PresencePublicService}
 */
public final class LastOnlineResponse extends AthenaContext {

    /**
     * The map for storing times by account ID.
     */
    private Map<String, Instant> lastOnline;

    public LastOnlineResponse(Map<String, Instant> lastOnline) {
        this.lastOnline = lastOnline;
    }

    /**
     * The map of last online, keyed by account ID.
     *
     * @return the map of times
     */
    public Map<String, Instant> lastOnline() {
        return lastOnline;
    }

    /**
     * Get a time by account ID.
     *
     * @param accountId the account ID.
     * @return the instant time
     */
    public Instant get(String accountId) {
        return lastOnline.get(accountId);
    }

    /**
     * Convert the provided {@code accountId} to a {@link Account} and return the time they were last online.
     *
     * @param accountId the account ID.
     * @return a {@link Pair} with the account and last online time (instant UTC).
     */
    public Pair<Account, Instant> getAndConvertToAccount(String accountId) {
        final var call = accountPublicService.findOneByAccountId(accountId);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Cannot find account " + accountId);
        return Pair.of(result[0], get(accountId));
    }

    /**
     * Map the lastOnline map to a map of accounts.
     *
     * @return a map with {@link Account} as keys.
     */
    public Map<Account, Instant> toAccounts() {
        final var accountIds = new ArrayList<>(lastOnline.keySet());
        final var map = new HashMap<Account, Instant>();

        // split account IDs into lists of 100 for bulk find.
        final var partitioned = Lists.partition(accountIds, 100);
        partitioned.forEach(list -> {
            // find and execute then put in map.
            final var call = accountPublicService.findManyByAccountId(list.toArray(String[]::new));
            final var result = Requests.executeCall(call);
            result.forEach(account -> map.put(account, lastOnline.get(account.accountId())));
        });
        return map;
    }

}
