package athena.account.resource;

import athena.account.resource.action.FriendAcceptor;
import athena.account.resource.external.ExternalAuth;
import athena.chat.FriendChat;
import athena.friend.Friends;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.friend.xmpp.event.events.FriendRequestEvent;
import athena.friend.xmpp.listener.FriendEventListener;
import athena.types.Platform;
import athena.util.json.hooks.PostDeserialize;
import athena.util.json.request.Request;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * Represents a Fortnite account.
 */
public final class Account {
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
    /**
     * The friends public service.
     */
    @Request(item = FriendsPublicService.class)
    private transient FriendsPublicService friendsPublicService;
    /**
     * The local account
     */
    @Request(item = Account.class, local = true)
    private transient Account account;
    /**
     * The friends provider
     */
    @Request(item = Friends.class)
    private transient Friends friends;
    /**
     * Friend chat provider.
     */
    @Request(item = FriendChat.class)
    private transient FriendChat chat;

    @PostDeserialize
    private void postDeserialize() {
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
        Requests.executeVoidCall(friendsPublicService.add(account.accountId(), accountId));
    }

    /**
     * Remove this account as a friend.
     */
    public void unfriend() {
        Requests.executeVoidCall(friendsPublicService.remove(account.accountId(), accountId));
    }

    /**
     * Block this account.
     */
    public void block() {
        Requests.executeVoidCall(friendsPublicService.block(account.accountId(), accountId));
    }

    /**
     * Unblock this account.
     */
    public void unblock() {
        Requests.executeVoidCall(friendsPublicService.unblock(account.accountId(), accountId));
    }

    /**
     * Retrieve the friend profile of this account.
     *
     * @return their {@link Profile}
     * @throws athena.exception.EpicGamesErrorException if an API error occurred or they are not a friend
     */
    public Profile friendProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), accountId, true));
    }

    /**
     * Automatically accept the incoming friend request from this account.
     *
     * @return a new {@link FriendAcceptor}
     */
    public FriendAcceptor acceptAsFriendAutomatically() {
        final var acceptor = new DefaultFriendAcceptor();
        friends.registerEventListenerForAccount(accountId, acceptor);
        return acceptor;
    }

    /**
     * Accepts a {@link FriendRequestEvent} when a friend request is sent from this account.
     *
     * @param consumer the consumer.
     */
    public void onFriendRequest(Consumer<FriendRequestEvent> consumer) {
        friends.registerEventListenerForAccount(accountId, new FriendEventListener() {
            @Override
            public void friendRequest(FriendRequestEvent event) {
                consumer.accept(event);
                friends.unregisterEventListenerForAccount(accountId, this);
            }
        });
    }

    /**
     * Add a friend listener just for this account.
     *
     * @param friendEventListener the listener.
     */
    public void addFriendListener(FriendEventListener friendEventListener) {
        friends.registerEventListenerForAccount(accountId, friendEventListener);
    }

    /**
     * Send a message to this account.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        chat.sendMessage(this, message);
    }

    /**
     * Provides a default implementation for {@link FriendAcceptor}
     */
    private final class DefaultFriendAcceptor implements FriendAcceptor, FriendEventListener {

        private Executor delayed;
        private Runnable action;
        private Consumer<Profile> profileConsumer;

        @Override
        public FriendAcceptor waitUntil(int seconds) {
            if (delayed == null) {
                delayed = CompletableFuture.delayedExecutor(seconds, TimeUnit.SECONDS);
                delayed.execute(() -> {
                    Account.this.friends.unregisterEventListenerForAccount(Account.this.accountId, this);
                    if (action != null) action.run();
                });
            } else {
                throw new UnsupportedOperationException("Wait until was already set!");
            }
            return this;
        }

        @Override
        public FriendAcceptor onExpired(Runnable run) {
            this.action = run;
            return this;
        }

        @Override
        public FriendAcceptor onAccepted(Consumer<Profile> profileConsumer) {
            this.profileConsumer = profileConsumer;
            return this;
        }

        @Override
        public void friendRequest(FriendRequestEvent event) {
            Account.this.friends.unregisterEventListenerForAccount(Account.this.accountId, this);
            final var profile = event.accept();
            if (profileConsumer != null) profileConsumer.accept(profile);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Account account = (Account) o;
        return account.accountId.equals(accountId);

    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(accountId)
                .toHashCode();
    }
}
