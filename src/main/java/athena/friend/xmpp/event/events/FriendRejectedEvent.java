package athena.friend.xmpp.event.events;

import athena.context.AthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.notification.Friendship;

/**
 * An event for when somebody rejects the friend request.
 */
public final class FriendRejectedEvent extends FriendEvent {
    public FriendRejectedEvent(Friendship notification, AthenaContext context) {
        super(notification, context);
    }
}
