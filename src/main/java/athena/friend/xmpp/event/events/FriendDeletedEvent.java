package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.types.friend.FriendApiObject;

/**
 * Invoked when somebody removes the account.
 */
public final class FriendDeletedEvent extends FriendEvent {

    public FriendDeletedEvent(FriendApiObject notification, DefaultAthenaContext context) {
        super(notification, context);
    }
}
