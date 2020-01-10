package athena.friend.xmpp.event.events;

import athena.context.AthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.notification.FriendType;

/**
 * Invoked when somebody removes the account.
 */
public final class FriendDeletedEvent extends FriendEvent {

    public FriendDeletedEvent(FriendType notification, AthenaContext context) {
        super(notification, context);
    }
}
