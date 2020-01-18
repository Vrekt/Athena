package athena.friend.xmpp.types.friend;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Provides support for the "FRIENDSHIP_XXX" type.
 */
public final class Friendship {

    /**
     * FRIENDSHIP_XXX
     * who its from
     * who its to
     * the timestamp
     */
    private String type, from, to;

    /**
     * The timestamp.
     */
    private Instant timestamp;

    /**
     * Status/reason, ex: "PENDING"/"REJECTED"
     */
    @SerializedName(value = "status", alternate = {"reason"})
    private String status;

    /**
     * @return the type
     */
    public String type() {
        return type;
    }

    /**
     * @return who its from
     */
    public String from() {
        return from;
    }

    /**
     * @return who its to
     */
    public String to() {
        return to;
    }

    /**
     * @return the timestamp
     */
    public Instant timestamp() {
        return timestamp;
    }

    /**
     * @return the status or reason.
     */
    public String status() {
        return status;
    }
}
