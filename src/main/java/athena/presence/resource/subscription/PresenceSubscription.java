package athena.presence.resource.subscription;

import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * A single presence subscription.
 */
public final class PresenceSubscription {

    /**
     * The account ID.
     */
    @SerializedName("account_id")
    private String accountId;
    /**
     * The time subscribed at.
     */
    @SerializedName("subscribed_at")
    private Instant subscribedAt;

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return The time subscribed at.
     */
    public Instant subscribedAt() {
        return subscribedAt;
    }
}
