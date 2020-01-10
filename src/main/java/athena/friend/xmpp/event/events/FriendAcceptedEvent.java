package athena.friend.xmpp.event.events;

import athena.context.AthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.notification.Friendship;

public final class FriendAcceptedEvent extends FriendEvent {

    public FriendAcceptedEvent(Friendship notification, AthenaContext context) {
        super(notification, context);
    }
}
