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
import com.google.gson.JsonElement;
import org.jivesoftware.smack.StanzaListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;

import java.util.ArrayList;
import java.util.List;
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
    private final EventFactory factory = EventFactory.createAnnotatedFactory(FriendEvent.class);
    /**
     * A list of all listeners registered.
     */
    private final CopyOnWriteArrayList<FriendEventListener> listeners = new CopyOnWriteArrayList<>();
    /**
     * Keeps a friend event listener for each account ID.
     */
    private final ConcurrentHashMap<String, List<FriendEventListener>> accountListeners = new ConcurrentHashMap<>();
    /**
     * The athena context.
     */
    private DefaultAthenaContext context;

    FriendsXMPPProvider(DefaultAthenaContext context) {
        this.context = context;
        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }


    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        // Make sure we don't have a JSON array.
        // This fixes a new issue discovered, epic changed their backend
        // to include a new message type that is an array - not an object.
        // 1/29/2020 - 6:08PM
        final var element = context.gson().fromJson(message.getBody(), JsonElement.class);
        if (element.isJsonArray()) return;
        final var object = element.getAsJsonObject();

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
            factory.invoke(FriendEvent.class, apiObject);
        } else if (friendType == FriendType.BLOCK_LIST_ENTRY_REMOVED) {
            listeners.forEach(listener -> listener.blockListEntryRemoved(apiObject));
            factory.invoke(FriendEvent.class, apiObject);
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
        final var list = accountListeners.getOrDefault(accountId, new ArrayList<>());
        list.add(listener);

        accountListeners.put(accountId, list);
    }

    /**
     * Unregister an event listener for the account {@code accountId}
     *
     * @param accountId the account ID.
     */
    void unregisterEventListenerForAccount(String accountId, FriendEventListener eventListener) {
        accountListeners.computeIfPresent(accountId, (k, v) -> {
            v.remove(eventListener);
            return v;
        });
    }

    /**
     * Unregister all event listeners.
     */
    void unregisterAllEventListeners() {
        factory.unregisterAll();
    }

    /**
     * Unregister all account event listeners.
     */
    void unregisterAllAccountEventListeners() {
        accountListeners.clear();
    }

    /**
     * Invoked after refreshing to re-add the stanza listener.
     *
     * @param context the new context.
     */
    void afterRefresh(DefaultAthenaContext context) {
        this.context = context;

        context
                .connectionManager()
                .connection()
                .addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    /**
     * Invoked before a refresh to remove the stanza listener.
     */
    void beforeRefresh() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);
    }

    /**
     * Invoked to shutdown this provider.
     */
    void shutdown() {
        context.connectionManager().connection().removeAsyncStanzaListener(this);

        factory.dispose();
        listeners.clear();
        accountListeners.clear();
    }


}
