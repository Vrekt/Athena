package athena.friend.resource.summary.types;

/**
 * Represents outgoing/incoming friend object.
 */
public final class BasicFriend {

    /**
     * The account ID and display name.
     * Display name is only ever present if the query ?displayNames=true is in the URL.
     */
    private String accountId, displayName;
    /**
     * {@code true} if they are a favorite - unimplemented
     */
    private boolean favorite;

    private BasicFriend() {}

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display name.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return {@code true} if they are a favorite - unimplemented
     */
    public boolean favorite() {
        return favorite;
    }
}
