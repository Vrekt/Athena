package athena.events.resource.player;

import java.util.List;

/**
 * Represents a response to the events tokens endpoint.
 */
public final class PlayerTokenResponse {

    /**
     * The list of accounts
     */
    private List<TokenPlayer> accounts;

    /**
     * Get the {@link TokenPlayer} from the {@code accountId}
     *
     * @param accountId the account ID
     * @return the {@link TokenPlayer} or {@code null} if not found.
     */
    public TokenPlayer get(String accountId) {
        return accounts.stream().filter(player -> player.accountId().equals(accountId)).findAny().orElse(null);
    }

    /**
     * @return the accounts
     */
    public List<TokenPlayer> accounts() {
        return accounts;
    }
}
