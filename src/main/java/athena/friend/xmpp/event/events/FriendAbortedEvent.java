package athena.friend.xmpp.event.events;

import athena.context.AthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.notification.Friendship;

/**
 * An event for when somebody cancels their friend request.
 */
public final class FriendAbortedEvent extends FriendEvent {
    public FriendAbortedEvent(Friendship notification, AthenaContext context) {
        super(notification, context);
    }
}