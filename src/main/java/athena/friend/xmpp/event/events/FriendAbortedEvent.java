package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.types.friend.Friendship;

/**
 * An event for when somebody cancels their friend request.
 */
public final class FriendAbortedEvent extends FriendEvent {
    public FriendAbortedEvent(Friendship notification, DefaultAthenaContext context) {
        super(notification, context);
    }
}