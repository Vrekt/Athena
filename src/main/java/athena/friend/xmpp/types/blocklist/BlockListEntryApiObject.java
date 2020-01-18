package athena.friend.xmpp.types.blocklist;

import java.time.Instant;

/**
 * Represents a blocklist entry api object.
 */
public final class BlockListEntryApiObject {

    /**
     * The payload
     */
    private Payload payload;
    /**
     * The timestamp
     */
    private Instant timestamp;

    /**
     * @return The owner who did the blocklist update
     */
    public String ownerId() {
        return payload.ownerId;
    }

    /**
     * @return who the blocklist update is for
     */
    public String accountId() {
        return payload.accountId;
    }

    /**
     * @return the timestamp
     */
    public Instant timestamp() {
        return timestamp;
    }

    private static final class Payload {
        /**
         * The owner who did the blocklist update
         * who the blocklist update is for
         */
        private String ownerId, accountId;
    }

}
