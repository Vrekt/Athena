package athena.friend;

import athena.context.AthenaContext;
import athena.friend.xmpp.annotation.FriendEvents;
import athena.friend.xmpp.event.events.*;
import athena.friend.xmpp.listener.FriendEventListener;
import athena.friend.xmpp.notification.FriendType;
import athena.friend.xmpp.notification.Friendship;
import athena.friend.xmpp.notification.type.FNotificationType;
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
 */
public final class XMPPProvider implements StanzaListener {

    /**
     * Provides event handling and registering/unregistering.
     */
    private final EventFactory factory = EventFactory.create(FriendEvents.class, 1);
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
    private final AthenaContext context;

    XMPPProvider(AthenaContext context) {
        this.context = context;
        context.connectionManager().connection().addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    /**
     * Initialize this provider from another one.
     *
     * @param other the other
     */
    XMPPProvider(AthenaContext context, XMPPProvider other) {
        this.context = context;
        this.listeners.addAll(other.listeners);
        this.accountListeners.putAll(other.accountListeners);

        context.connectionManager().connection().addAsyncStanzaListener(this, MessageTypeFilter.NORMAL);
    }

    @Override
    public void processStanza(Stanza packet) {
        final var message = (Message) packet;
        final var object = context.gson().fromJson(message.getBody(), JsonObject.class);
        final var type = object.getAsJsonPrimitive("type").getAsString();
        final var of = FNotificationType.typeOf(type);
        if (of == FNotificationType.UNKNOWN) return;

        if (of == FNotificationType.FRIEND || of == FNotificationType.FRIEND_REMOVAL) {
            final var notification = context.gson().fromJson(message.getBody(), FriendType.class);
            handleFriendType(notification);
        } else {
            final var notification = context.gson().fromJson(message.getBody(), Friendship.class);
            handleFriendship(notification);
        }
    }

    /**
     * Handle the friend type notification.
     *
     * @param notification the notification.
     */
    private void handleFriendType(FriendType notification) {
        final var status = notification.status();
        final var direction = notification.direction();
        if (direction == null || direction.equals("OUTBOUND")) return;

        switch (status) {
            case "PENDING":
                final var friendRequestEvent = new FriendRequestEvent(notification, context);
                listeners.forEach(listener -> listener.friendRequest(friendRequestEvent));
                factory.invoke(friendRequestEvent);

                if (accountListeners.containsKey(notification.accountId()))
                    accountListeners.get(notification.accountId()).friendRequest(friendRequestEvent);
                break;
            case "DELETED":
                final var friendDeletedEvent = new FriendDeletedEvent(notification, context);
                listeners.forEach(listener -> listener.friendDeleted(friendDeletedEvent));
                factory.invoke(friendDeletedEvent);

                if (accountListeners.containsKey(notification.accountId()))
                    accountListeners.get(notification.accountId()).friendDeleted(friendDeletedEvent);
                break;
        }
    }

    /**
     * Handle the friendship notification
     * TODO: Possibly change behaviour, right now if you accept a friend request it will fire events.
     * TODO: Maybe its only desirable to fire events if its from someone else (they accepted, they rejected, etc).
     *
     * @param notification the notification.
     */
    private void handleFriendship(Friendship notification) {
        final var status = notification.status();
        switch (status) {
            case "ABORTED":
                final var friendAbortedEvent = new FriendAbortedEvent(notification, context);
                listeners.forEach(listener -> listener.friendAborted(friendAbortedEvent));
                factory.invoke(friendAbortedEvent);

                if (accountListeners.containsKey(notification.from()))
                    accountListeners.get(notification.from()).friendAborted(friendAbortedEvent);
                break;
            case "ACCEPTED":
                final var friendAcceptedEvent = new FriendAcceptedEvent(notification, context);
                listeners.forEach(listener -> listener.friendAccepted(friendAcceptedEvent));
                factory.invoke(friendAcceptedEvent);

                if (accountListeners.containsKey(notification.from()))
                    accountListeners.get(notification.from()).friendAccepted(friendAcceptedEvent);
                break;
            case "REJECTED":
                final var friendRejectedEvent = new FriendRejectedEvent(notification, context);
                listeners.forEach(listener -> listener.friendRejected(friendRejectedEvent));
                factory.invoke(friendRejectedEvent);

                if (accountListeners.containsKey(notification.from()))
                    accountListeners.get(notification.from()).friendRejected(friendRejectedEvent);
                break;
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
