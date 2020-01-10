package athena.friend.xmpp.notification;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Provides support for the "com.epicgames.friends.core.apiobjects.Friend" type.
 */
public final class FriendType {

    /**
     * The type "com.epicgames.friends.core.apiobjects.Friend"
     */
    private String type;
    /**
     * The timestamp
     */
    private Instant timestamp;

    /**
     * The payload.
     */
    private Payload payload;

    /**
     * @return the type.
     */
    public String type() {
        return type;
    }

    /**
     * @return the timestamp.
     */
    public Instant timestamp() {
        return timestamp;
    }

    /**
     * @return the account ID of who its from.
     */
    public String accountId() {
        return payload.accountId;
    }

    /**
     * @return the direction
     */
    public String direction() {
        return payload.direction;
    }

    /**
     * @return when it was created.
     */
    public Instant created() {
        return payload.created;
    }

    /**
     * @return the status or reason
     */
    public String status() {
        return payload.status;
    }

    /**
     * @return {@code true} if they are a favorite.
     */
    public boolean favorite() {
        return payload.favorite;
    }

    private static final class Payload {

        /**
         * "XXX",
         * "OUTBOUND/INBOUND",
         * UTC created.
         */
        private String accountId, direction;
        /**
         * When it was created.
         */
        private Instant created;
        /**
         * Status/reason, ex: "PENDING"/"REJECTED"
         */
        @SerializedName(value = "status", alternate = {"reason"})
        private String status;

        /**
         * {@code true} if they are a favorite.
         */
        private boolean favorite;
    }

}
