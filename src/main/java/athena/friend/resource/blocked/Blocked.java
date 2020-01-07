package athena.friend.resource.blocked;

/**
 * Represents a blocked account ID.
 */
public final class Blocked {

    /**
     * The account ID.
     */
    private String accountId;

    private Blocked() {
    }

    /**
     * @return the account ID of this blocked account.
     */
    public String accountId() {
        return accountId;
    }
}
