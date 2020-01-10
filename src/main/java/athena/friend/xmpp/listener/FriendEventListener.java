package athena.friend.xmpp.listener;

import athena.friend.xmpp.event.events.*;

/**
 * Interface used to listen for friend events.
 */
public interface FriendEventListener {

    /**
     * Invoked when a friend request is received.
     *
     * @param event the event.
     */
    default void friendRequest(FriendRequestEvent event) {
    }

    /**
     * Invoked when a friend removes you.
     *
     * @param event the event.
     */
    default void friendDeleted(FriendDeletedEvent event) {
    }

    /**
     * Invoked when a friend rejects the friend request.
     *
     * @param event the event.
     */
    default void friendRejected(FriendRejectedEvent event) {
    }

    /**
     * Invoked when a friend cancels their friend request.
     *
     * @param event the event.
     */
    default void friendAborted(FriendAbortedEvent event) {
    }

    /**
     * Invoked when a friend accepts the friend request.
     *
     * @param event the event.
     */
    default void friendAccepted(FriendAcceptedEvent event) {
    }

}
