package athena.friend.xmpp.listener;

import athena.friend.xmpp.event.events.*;
import athena.friend.xmpp.types.blocklist.BlockListEntry;
import athena.friend.xmpp.types.blocklist.BlockListUpdate;

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

    /**
     * Invoked when a block list entry is removed.
     * Either method is fine to use
     *
     * @param blockListEntryApiObject the API object.
     */
    default void blockListEntryRemoved(BlockListEntry blockListEntryApiObject) {

    }

    /**
     * Invoked when a block list entry is removed.
     * Either method is fine to use
     *
     * @param blockListUpdate the block list update.
     */
    default void blockListEntryRemoved(BlockListUpdate blockListUpdate) {

    }

    /**
     * Invoked when a block list entry is added.
     * Either method is fine to use
     *
     * @param blockListEntryApiObject the API object.
     */
    default void blockListEntryAdded(BlockListEntry blockListEntryApiObject) {

    }

    /**
     * Invoked when a block list entry is removed.
     * Either method is fine to use
     *
     * @param blockListUpdate the block list update.
     */
    default void blockListEntryAdded(BlockListUpdate blockListUpdate) {

    }

}
