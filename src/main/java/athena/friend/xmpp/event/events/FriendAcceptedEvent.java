package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.Friendship;

/**
 * An event for when a friend request is accepted.
 */
public final class FriendAcceptedEvent extends AbstractFriendEvent {

    public FriendAcceptedEvent(Friendship friendship, FriendType friendType, DefaultAthenaContext context) {
        super(friendship, friendType, context);
    }
}
