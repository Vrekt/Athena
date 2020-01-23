package athena.friend;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.annotation.FriendEvent;
import athena.friend.xmpp.event.events.*;
import athena.friend.xmpp.listener.FriendEventListener;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.blocklist.BlockListEntryApiObject;
import athena.friend.xmpp.types.blocklist.BlockListUpdate;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.friend.xmpp.types.friend.Friendship;
import athena.util.event.EventFactory;
import com.google.gson.JsonObject;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Provides XMPP access to {@link athena.friend.Friends}
 *
 * @author Vrekt, Terbau
 */
public final class FriendsXMPPProvider implements StanzaListener {

    /**
     * Provides event handling and registering/unregistering.
     */
    private final EventFactory factory;
    /**
     * A list of all listeners registered.
     */
    private final CopyOnWriteArrayList<FriendEventListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * Keeps a friend event listener for each account ID.
     */
    private final ConcurrentHashMap<String, FriendEventListener> accountListeners = new ConcurrentHashMap<>();
    /**
     * The athena context.
     */
    private final DefaultAthenaContext context;

    FriendsXMPPProvider(DefaultAthenaContext context) {
        this.context = context;
        this.factory = EventFactory.create(FriendEvent.class, 1);
        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    /**
     * Initialize this provider from another one.
     *
     * @param other the other
     */
    FriendsXMPPProvider(DefaultAthenaContext context, FriendsXMPPProvider other) {
        this.context = context;
        this.listeners.addAll(other.listeners);
        this.accountListeners.putAll(other.accountListeners);
        this.factory = EventFactory.create(other.factory);
        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        final var object = context.gson().fromJson(message.getBody(), JsonObject.class);
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
                final var blockListEntry = context.gson().fromJson(message.getBody(), BlockListEntryApiObject.class);
                blockListApiObject(blockListEntry, of);
                break;
            case USER_BLOCKLIST_UPDATE:
                final var blockListUpdate = context.gson().fromJson(message.getBody(), BlockListUpdate.class);
                blockListUpdate(blockListUpdate);
                break;
        }
    }

    /**
     * Handles the ty[e {@link BlockListEntryApiObject}
     * TODO: Refactor at some point.
     * TODO: Currently users must manually distinguish between blocked/unblocked events for annotated methods.
     *
     * @param apiObject  the API object.
     * @param friendType the friend-type.
     */
    private void blockListApiObject(BlockListEntryApiObject apiObject, FriendType friendType) {
        if (friendType == FriendType.BLOCK_LIST_ENTRY_ADDED) {
            listeners.forEach(listener -> listener.blockListEntryAdded(apiObject));
            factory.invoke(apiObject);
        } else if (friendType == FriendType.BLOCK_LIST_ENTRY_REMOVED) {
            listeners.forEach(listener -> listener.blockListEntryRemoved(apiObject));
            factory.invoke(apiObject);
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
            factory.invoke(blockListUpdate);
            // user was unblocked.
        } else if (status.equalsIgnoreCase("UNBLOCKED")) {
            listeners.forEach(listener -> listener.blockListEntryRemoved(blockListUpdate));
            factory.invoke(blockListUpdate);
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
            factory.invoke(event);

            if (accountListeners.containsKey(event.accountId())) accountListeners.get(event.accountId()).friendRequest(event);
            // if a friend is deleted.
        } else if (status.equalsIgnoreCase("DELETED")) {
            final var event = new FriendDeletedEvent(friendApiObject, friendType, context);
            listeners.forEach(listener -> listener.friendDeleted(event));
            factory.invoke(event);

            if (accountListeners.containsKey(event.accountId())) accountListeners.get(event.accountId()).friendDeleted(event);
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
            factory.invoke(event);

            if (accountListeners.containsKey(event.accountId())) accountListeners.get(event.accountId()).friendAborted(event);
            // the friend request was accepted.
        } else if (status.equalsIgnoreCase("ACCEPTED")) {
            final var event = new FriendAcceptedEvent(friendship, friendType, context);
            listeners.forEach(listener -> listener.friendAccepted(event));
            factory.invoke(event);

            if (accountListeners.containsKey(event.accountId())) accountListeners.get(event.accountId()).friendAccepted(event);
            // the friend request was rejected.
        } else if (status.equalsIgnoreCase("REJECTED")) {
            final var event = new FriendRejectedEvent(friendship, friendType, context);
            listeners.forEach(listener -> listener.friendRejected(event));
            factory.invoke(event);

            if (accountListeners.containsKey(event.accountId())) accountListeners.get(event.accountId()).friendRejected(event);
        }
    }

    /**
     * Register an event listener.
     *
     * @param type the class/type to register.
     */
    void registerEventListener(Object type) {
        factory.registerEventListener(type);
    }

    /**
     * Unregister an event listener.
     *
     * @param type the class/type to register.
     */
    void unregisterEventListener(Object type) {
        factory.unregisterEventListener(type);
    }

    /**
     * Register an event listener.
     *
     * @param listener the listener.
     */
    void registerEventListener(FriendEventListener listener) {
        listeners.add(listener);
    }

    /**
     * Unregister an event listener.
     *
     * @param listener the listener
     */
    void unregisterEventListener(FriendEventListener listener) {
        listeners.remove(listener);
    }

    /**
     * Register an event listener for the specified {@code accountId}
     *
     * @param accountId the account ID.
     * @param listener  the listener.
     */
    void registerEventListenerForAccount(String accountId, FriendEventListener listener) {
        accountListeners.put(accountId, listener);
    }

    /**
     * Unregister an event listener for the account {@code accountId}
     *
     * @param accountId the account ID.
     */
    void unregisterEventListenerForAccount(String accountId) {
        accountListeners.remove(accountId);
    }

    /**
     * Removes the stanza listener.
     */
    void removeStanzaListener() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Close this provider.
     */
    void close() {
        removeStanzaListener();

        factory.dispose();
        listeners.clear();
        accountListeners.clear();
    }

    /**
     * Clean.
     */
    void clean() {
        removeStanzaListener();
    }

}
