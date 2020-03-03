package athena.friend.xmpp.event.events;

import athena.context.DefaultAthenaContext;
import athena.friend.resource.summary.Profile;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.util.request.Requests;

/**
 * An event that is invoked when a friend request is received.
 */
public final class FriendRequestEvent extends AbstractFriendEvent {

    public FriendRequestEvent(FriendApiObject friendApiObject, FriendType friendType, DefaultAthenaContext context) {
        super(friendApiObject, friendType, context);
    }

    /**
     * Accept the friend request.
     *
     * @return their profile, or {@code null} if none was found.
     */
    public Profile accept() {
        Requests.executeVoidCall(context.friendsService().add(context.localAccountId(), accountId));
        return Requests.executeCall(context.friendsService().profile(context.localAccountId(), accountId, true));
    }


    /**
     * Decline the friend request.
     */
    public void decline() {
        Requests.executeVoidCall(context.friendsService().remove(context.localAccountId(), accountId));
    }

}
