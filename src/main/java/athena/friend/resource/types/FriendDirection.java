package athena.friend.resource.types;

/**
 * An enum of friend directions.
 */
public enum FriendDirection {

    OUTBOUND, INBOUND, UNKNOWN;

    /**
     * @return {@code true} if this friend request was sent by the current account.
     */
    public boolean sent() {
        return this == OUTBOUND;
    }

    /**
     * @return {@code true} if this friend request was received by the friend.
     */
    public boolean received() {
        return this == INBOUND;
    }

}
