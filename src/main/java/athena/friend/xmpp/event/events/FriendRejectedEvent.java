package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.types.friend.Friendship;

/**
 * An event for when somebody rejects the friend request.
 */
public final class FriendRejectedEvent extends FriendEvent {
    public FriendRejectedEvent(Friendship notification, DefaultAthenaContext context) {
        super(notification, context);
    }
}
