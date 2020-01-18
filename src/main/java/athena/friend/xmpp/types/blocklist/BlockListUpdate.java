package athena.friend.xmpp.types.blocklist;

import java.time.Instant;

/**
 * Represents a blocklist update
 */
public final class BlockListUpdate {

    /**
     * The owner who did the blocklist update
     * who the blocklist update is for
     * the status, "BLOCKED" "UNBLOCKED"
     */
    private String ownerId, accountId, status;
    /**
     * The timestamp.
     */
    private Instant timestamp;

    /**
     * @return The owner who did the blocklist update
     */
    public String ownerId() {
        return ownerId;
    }

    /**
     * @return who the blocklist update is for
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the status "BLOCKED" "UNBLOCKED"
     */
    public String status() {
        return status;
    }

    /**
     * @return the timestamp
     */
    public Instant timestamp() {
        return timestamp;
    }
}
