package athena.account.resource;

import athena.account.resource.external.ExternalAuth;
import athena.friend.xmpp.event.events.FriendRequestEvent;
import athena.friend.xmpp.listener.FriendEventListener;
import athena.types.Platform;
import athena.util.json.post.annotation.PostDeserialize;
import athena.util.request.Requests;
import athena.context.AthenaContext;
import com.google.gson.annotations.SerializedName;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Represents a Fortnite account.
 */
public final class Account extends AthenaContext {
    /**
     * The account ID for this account.
     */
    @SerializedName("id")
    private String accountId;
    /**
     * The display name of this account.
     */
    private String displayName;
    /**
     * A list of external auths.
     */
    private Map<String, ExternalAuth> externalAuths;
    /**
     * Bare JID of this account.
     */
    private BareJid jid;

    private Account() {
    }

    @PostDeserialize
    private void post() {
        jid = JidCreate.bareFromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com");
    }

    /**
     * @return the ID of this account.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display name of this account.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return a map of external auths.
     */
    public Map<String, ExternalAuth> externalAuths() {
        return externalAuths;
    }

    /**
     * @return the JID of this account.
     */
    public BareJid jid() {
        return jid;
    }

    /**
     * Checks if the external auth is present.
     *
     * @param platform the platform, ex: "psn, "xbl"
     * @return {@code true} if the external auth is present.
     */
    public boolean hasExternalAuth(String platform) {
        return externalAuths.containsKey(platform);
    }

    /**
     * Checks if the external auth is present.
     *
     * @param platform the platform.
     * @return {@code true} if the external auth is present.
     */
    public boolean hasExternalAuth(Platform platform) {
        return externalAuths.values().stream().anyMatch(externalAuth -> externalAuth.platform() == platform);
    }

    /**
     * Get the external auth.
     *
     * @param platform the platform, ex: "psn, "xbl"
     * @return the external auth or {@code null} if not found.
     */
    public ExternalAuth getExternalAuth(String platform) {
        return externalAuths.get(platform);
    }

    /**
     * Get the external auth.
     *
     * @param platform the platform
     * @return the {@link ExternalAuth} or {@code null} if not found.
     */
    public ExternalAuth getExternalAuth(Platform platform) {
        return externalAuths.values().stream().filter(externalAuth -> externalAuth.platform() == platform).findAny().orElse(null);
    }

    /**
     * Add this account as a friend.
     */
    public void friend() {
        Requests.executeVoidCall(friendsPublicService.add(localAccountId, accountId));
    }

    /**
     * Remove this account as a friend.
     */
    public void unfriend() {
        Requests.executeVoidCall(friendsPublicService.remove(localAccountId, accountId));
    }

    /**
     * Block this account.
     */
    public void block() {
        Requests.executeVoidCall(friendsPublicService.block(localAccountId, accountId));
    }

    /**
     * Unblock this account.
     */
    public void unblock() {
        Requests.executeVoidCall(friendsPublicService.unblock(localAccountId, accountId));
    }

    /**
     * Add an incoming friend request listener.
     *
     * @param consumer the consumer
     */
    public void addIncomingFriendRequestListener(Consumer<FriendRequestEvent> consumer) {
        friends.registerEventListenerForAccount(accountId, new FriendEventListener() {
            @Override
            public void friendRequest(FriendRequestEvent event) {
                consumer.accept(event);
            }
        });
    }

    /**
     * Remove the incoming friend request listener.
     */
    public void removeIncomingFriendRequestListener() {
        friends.unregisterEventListenerForAccount(accountId);
    }

    /**
     * Add a friend listener just for this account.
     * If you already have a listener via {@code addIncomingFriendRequestListener} then this listener will overwrite that one.
     *
     * @param friendEventListener the listener.
     */
    public void addFriendListener(FriendEventListener friendEventListener) {
        friends.registerEventListenerForAccount(accountId, friendEventListener);
    }
}
