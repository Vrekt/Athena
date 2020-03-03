package athena.events.resource.player;

import java.util.List;

/**
 * Represents an account/player inside the token response
 */
public final class TokenPlayer {

    /**
     * The account ID of this player
     */
    private String accountId;

    /**
     * The list of tokens
     */
    private List<String> tokens;

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the list of tokens
     */
    public List<String> tokens() {
        return tokens;
    }
}
