package athena.friend;

import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.Friend;
import athena.friend.resource.blocked.Blocked;
import athena.friend.resource.settings.FriendSettings;
import athena.friend.resource.summary.Profile;
import athena.friend.resource.summary.Summary;
import athena.friend.service.FriendsPublicService;
import athena.friend.xmpp.annotation.FriendEvent;
import athena.friend.xmpp.event.events.*;
import athena.friend.xmpp.listener.FriendEventListener;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.blocklist.BlockListEntry;
import athena.friend.xmpp.types.blocklist.BlockListUpdate;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.friend.xmpp.types.friend.Friendship;
import athena.util.event.EventFactory;
import athena.util.request.Requests;
import com.google.gson.JsonElement;
import okhttp3.RequestBody;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Provides easy access to the {@link FriendsPublicService} and XMPP.
 */
public final class Friends implements Closeable {

    /**
     * The athena context.
     */
    private final DefaultAthenaContext context;
    /**
     * The service.
     */
    private final FriendsPublicService service;

    /**
     * The XMPP event listener.
     */
    private final Listener eventListener = new Listener();

    /**
     * Provides event handling and registering/unregistering.
     */
    private final EventFactory factory = EventFactory.createAnnotatedFactory(FriendEvent.class);
    /**
     * A list of all listeners registered.
     */
    private final CopyOnWriteArrayList<FriendEventListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * Keeps a friend event listener for each account ID.
     */
    private final ConcurrentHashMap<String, List<FriendEventListener>> accountListeners = new ConcurrentHashMap<>();

    public Friends(DefaultAthenaContext context) {
        this.context = context;
        this.service = context.friendsService();
        if (context.xmppEnabled()) context.connection().addAsyncStanzaListener(eventListener, MessageTypeFilter.NORMAL);
    }

    /**
     * @return the service that is tied to this class.
     */
    public FriendsPublicService service() {
        return service;
    }

    /**
     * Adds a friend via their account ID.
     *
     * @param accountId the ID of the account
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void add(String accountId) throws EpicGamesErrorException {
        final var call = service.add(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Removes a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeOrDecline(String accountId) throws EpicGamesErrorException {
        final var call = service.remove(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Blocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void block(String accountId) throws EpicGamesErrorException {
        final var call = service.block(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unblocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void unblock(String accountId) throws EpicGamesErrorException {
        final var call = service.unblock(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Gets a list of all friends.
     *
     * @param includePending {@code true} if pending friend requests should be included.
     * @return a list of friends
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Friend> friends(boolean includePending) throws EpicGamesErrorException {
        final var call = service.friends(context.localAccountId(), includePending);
        return Requests.executeCall(call);
    }

    /**
     * Gets a list of all blocked friends.
     *
     * @return a list of all blocked friends (account IDs)
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<String> blocked() throws EpicGamesErrorException {
        final var call = service.blocked(context.localAccountId());
        return Requests.executeCall(call).stream().map(Blocked::accountId).collect(Collectors.toList());
    }

    /**
     * Sets the alias (nickname) of a friend.
     *
     * @param accountId the account ID of the friend
     * @param alias     the alias (nickname) to put.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendAlias(String accountId, String alias) throws EpicGamesErrorException {
        if (alias.length() < 3 || alias.length() > 16) throw new IllegalArgumentException("Alias must be 3 characters minimum and 16 characters maximum.");
        final var call = service.setAlias(context.localAccountId(), accountId, RequestBody.create(alias, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Sets the alias (nickname) of a friend.
     *
     * @param friend the friend
     * @param alias  the alias (nickname) to put.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendAlias(Friend friend, String alias) throws EpicGamesErrorException {
        setFriendAlias(friend.accountId(), alias);
    }

    /**
     * Removes the alias of a friend.
     *
     * @param accountId the account ID of the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendAlias(String accountId) throws EpicGamesErrorException {
        final var call = service.remove(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Removes the alias of a friend.
     *
     * @param friend the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendAlias(Friend friend) throws EpicGamesErrorException {
        removeFriendAlias(friend.accountId());
    }

    /**
     * Sets the note of a friend.
     *
     * @param accountId the account ID of the friend
     * @param note      the note
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendNote(String accountId, String note) throws EpicGamesErrorException {
        if (note.length() < 3 || note.length() > 255) throw new IllegalArgumentException("Note must be 3 characters minimum and 255 characters maximum.");
        final var call = service.setNote(context.localAccountId(), accountId, RequestBody.create(note, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Sets a friend note
     *
     * @param friend the friend
     * @param note   the note
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendNote(Friend friend, String note) throws EpicGamesErrorException {
        setFriendNote(friend.accountId(), note);
    }

    /**
     * Removes a friend note
     *
     * @param accountId the account ID of the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendNote(String accountId) throws EpicGamesErrorException {
        final var call = service.remove(context.localAccountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Removes a friend note
     *
     * @param friend the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendNote(Friend friend) throws EpicGamesErrorException {
        removeFriendNote(friend.accountId());
    }

    /**
     * Get a friend profile.
     *
     * @param accountId the account ID of the friend.
     * @return a {@link Profile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Profile friendProfile(String accountId) throws EpicGamesErrorException {
        final var call = service.profile(context.localAccountId(), accountId, true);
        return Requests.executeCall(call);
    }

    /**
     * Get a friend profile.
     *
     * @param friend the friend
     * @return a {@link Profile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Profile friendProfile(Friend friend) throws EpicGamesErrorException {
        return friendProfile(friend.accountId());
    }

    /**
     * Get the friend summary.
     *
     * @return a {@link Summary} representing the response.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Summary summary() throws EpicGamesErrorException {
        final var call = service.summary(context.localAccountId(), true);
        return Requests.executeCall(call);
    }

    /**
     * Get the current friend settings.
     *
     * @return a {@link FriendSettings} that represents the settings.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings settings() throws EpicGamesErrorException {
        final var call = service.settings(context.localAccountId());
        return Requests.executeCall(call);
    }

    /**
     * Set the friend settings
     *
     * @param settings the settings
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings setSettings(FriendSettings settings) throws EpicGamesErrorException {
        final var call = service.setSettings(context.localAccountId(), settings);
        return Requests.executeCall(call);
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    public void registerEventListener(Object type) {
        factory.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    public void unregisterEventListener(Object type) {
        factory.unregisterEventListener(type);
    }

    /**
     * Register an event listener.
     *
     * @param listener the listener.
     */
    public void registerEventListener(FriendEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregister an event listener.
     *
     * @param listener the listener
     */
    public void unregisterEventListener(FriendEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Register an event listener for the specified {@code accountId}
     *
     * @param accountId the account ID.
     * @param listener  the listener.
     */
    public void registerEventListenerForAccount(String accountId, FriendEventListener listener) {
        final var list = accountListeners.getOrDefault(accountId, new ArrayList<>());
        list.add(listener);

        accountListeners.put(accountId, list);
    }

    /**
     * Unregister an event listener for the account {@code accountId}
     *
     * @param accountId the account ID.
     */
    public void unregisterEventListenerForAccount(String accountId, FriendEventListener eventListener) {
        accountListeners.computeIfPresent(accountId, (k, v) -> {
            v.remove(eventListener);
            return v;
        });
    }

    /**
     * Unregister all event listeners.
     */
    public void unregisterAllEventListeners() {
        factory.unregisterAll();
    }

    /**
     * Unregister all account event listeners.
     */
    public void unregisterAllAccountEventListeners() {
        accountListeners.clear();
    }

    @Override
    public void close() {
        if (context.xmppEnabled()) context.connection().removeAsyncStanzaListener(eventListener);

        factory.dispose();
        listeners.clear();
        accountListeners.clear();
    }

    /**
     * The XMPP event listener.
     */
    private final class Listener implements StanzaListener {
        @Override
        public void processStanza(Stanza packet) {
            final var message = (Message) packet;
            // Make sure we don't have a JSON array.
            // This fixes a new issue discovered, epic changed their backend
            // to include a new message type that is an array - not an object.
            // 1/29/2020 - 6:08PM
            final var element = context.gson().fromJson(message.getBody(), JsonElement.class);
            final var object = element.getAsJsonObject();
            if (object.has("interactions")) return; // ignore interactions types.
            final var type = object.getAsJsonPrimitive("type").getAsString();
            final var of = FriendType.typeOf(type);
            if (of == FriendType.UNKNOWN) return;

            switch (of) {
                case FRIEND:
                case FRIEND_REMOVAL:
                    final var friendApiObject = context.gson().fromJson(message.getBody(), FriendApiObject.class);
                    friendApiObject(friendApiObject, of);
                    break;
                case FRIENDSHIP_REQUEST:
                case FRIENDSHIP_REMOVE:
                    final var friendship = context.gson().fromJson(message.getBody(), Friendship.class);
                    friendship(friendship, of);
                    break;
                case BLOCK_LIST_ENTRY_ADDED:
                case BLOCK_LIST_ENTRY_REMOVED:
                    final var blockListEntry = context.gson().fromJson(message.getBody(), BlockListEntry.class);
                    blockListEntry(blockListEntry, of);
                    break;
                case USER_BLOCKLIST_UPDATE:
                    final var blockListUpdate = context.gson().fromJson(message.getBody(), BlockListUpdate.class);
                    blockListUpdate(blockListUpdate);
                    break;
            }
        }

        /**
         * Handles the type {@link BlockListEntry}
         * TODO: Refactor at some point.
         * TODO: Currently users must manually distinguish between blocked/unblocked events for annotated methods.
         *
         * @param entry      the entry
         * @param friendType the friend-type.
         */
        private void blockListEntry(BlockListEntry entry, FriendType friendType) {
            if (friendType == FriendType.BLOCK_LIST_ENTRY_ADDED) {
                listeners.forEach(listener -> listener.blockListEntryAdded(entry));
                factory.invoke(FriendEvent.class, entry);
            } else if (friendType == FriendType.BLOCK_LIST_ENTRY_REMOVED) {
                listeners.forEach(listener -> listener.blockListEntryRemoved(entry));
                factory.invoke(FriendEvent.class, entry);
            }
        }

        /**
         * Handles the type {@link BlockListUpdate}
         * TODO: Refactor at some point.
         * TODO: Currently users must manually distinguish between blocked/unblocked events for annotated methods.
         *
         * @param blockListUpdate the blocklist update
         */
        private void blockListUpdate(BlockListUpdate blockListUpdate) {
            final var status = blockListUpdate.status();
            // user was blocked.
            if (status.equalsIgnoreCase("BLOCKED")) {
                listeners.forEach(listener -> listener.blockListEntryAdded(blockListUpdate));
                factory.invoke(FriendEvent.class, blockListUpdate);
                // user was unblocked.
            } else if (status.equalsIgnoreCase("UNBLOCKED")) {
                listeners.forEach(listener -> listener.blockListEntryRemoved(blockListUpdate));
                factory.invoke(FriendEvent.class, blockListUpdate);
            }
        }

        /**
         * Handle the type {@link FriendApiObject}
         * TODO: Hopefully refactor this in the future
         *
         * @param friendApiObject the API object.
         */
        private void friendApiObject(FriendApiObject friendApiObject, FriendType friendType) {
            final var direction = friendApiObject.direction();
            // ensure the notification is valid for us.
            if (direction == null || direction.equalsIgnoreCase("OUTBOUND")) return;
            final var status = friendApiObject.status();

            // if a pending friend request is incoming.
            if (status.equalsIgnoreCase("PENDING")) {
                final var event = new FriendRequestEvent(friendApiObject, friendType, context);
                listeners.forEach(listener -> listener.friendRequest(event));
                factory.invoke(FriendEvent.class, event);

                if (accountListeners.containsKey(event.accountId()))
                    accountListeners
                            .get(event.accountId())
                            .forEach(listener -> listener.friendRequest(event));
                // if a friend is deleted.
            } else if (status.equalsIgnoreCase("DELETED")) {
                final var event = new FriendDeletedEvent(friendApiObject, friendType, context);
                listeners.forEach(listener -> listener.friendDeleted(event));
                factory.invoke(FriendEvent.class, event);

                if (accountListeners.containsKey(event.accountId()))
                    accountListeners
                            .get(event.accountId())
                            .forEach(listener -> listener.friendDeleted(event));
            }

        }

        /**
         * Handle the type {@link Friendship}
         * TODO: Hopefully refactor this in the future.
         * TODO: Possibly change behaviour, right now if you accept a friend request it will fire events.
         * TODO: Maybe its only desirable to fire events if its from someone else (they accepted, they rejected, etc).
         *
         * @param friendship the friendship
         */
        private void friendship(Friendship friendship, FriendType friendType) {
            final var status = friendship.status();

            // the friend request was aborted.
            if (status.equalsIgnoreCase("ABORTED")) {
                final var event = new FriendAbortedEvent(friendship, friendType, context);
                listeners.forEach(listener -> listener.friendAborted(event));
                factory.invoke(FriendEvent.class, event);

                if (accountListeners.containsKey(event.accountId()))
                    accountListeners
                            .get(event.accountId())
                            .forEach(listener -> listener.friendAborted(event));
                // the friend request was accepted.
            } else if (status.equalsIgnoreCase("ACCEPTED")) {
                final var event = new FriendAcceptedEvent(friendship, friendType, context);
                listeners.forEach(listener -> listener.friendAccepted(event));
                factory.invoke(FriendEvent.class, event);

                if (accountListeners.containsKey(event.accountId()))
                    accountListeners
                            .get(event.accountId())
                            .forEach(listener -> listener.friendAccepted(event));
                // the friend request was rejected.
            } else if (status.equalsIgnoreCase("REJECTED")) {
                final var event = new FriendRejectedEvent(friendship, friendType, context);
                listeners.forEach(listener -> listener.friendRejected(event));
                factory.invoke(FriendEvent.class, event);

                if (accountListeners.containsKey(event.accountId()))
                    accountListeners
                            .get(event.accountId())
                            .forEach(listener -> listener.friendRejected(event));
            }
        }
    }

}
