package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.types.friend.Friendship;

public final class FriendAcceptedEvent extends FriendEvent {

    public FriendAcceptedEvent(Friendship notification, DefaultAthenaContext context) {
        super(notification, context);
    }
}
