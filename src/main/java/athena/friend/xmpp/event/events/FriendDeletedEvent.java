package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.FriendApiObject;

/**
 * An event for when a friend is deleted.
 */
public final class FriendDeletedEvent extends AbstractFriendEvent {

    public FriendDeletedEvent(FriendApiObject friendApiObject, FriendType friendType, DefaultAthenaContext context) {
        super(friendApiObject, friendType, context);
    }
}
