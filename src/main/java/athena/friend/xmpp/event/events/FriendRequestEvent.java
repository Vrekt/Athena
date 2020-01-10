package athena.friend.xmpp.event.events;

import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.resource.summary.Profile;
import athena.friend.xmpp.event.FriendEvent;
import athena.friend.xmpp.notification.FriendType;
import athena.util.request.Requests;

/**
 * An event that is invoked when a friend request is received.
 */
public final class FriendRequestEvent extends FriendEvent {

    public FriendRequestEvent(FriendType notification, AthenaContext context) {
        super(notification, context);
    }

    /**
     * Accept the friend request.
     *
     * @return their profile.
     */
    public Profile accept() {
        Requests.executeVoidCall(context.friendsService().add(localAccountId, accountId));
        return Requests.executeCallOptional(context.friendsService().profile(localAccountId, accountId, true))
                .orElseThrow(() -> EpicGamesErrorException.create("Cannot retrieve profile for " + accountId));
    }


    /**
     * Decline the friend request.
     */
    public void decline() {
        Requests.executeVoidCall(context.friendsService().remove(localAccountId, accountId));
    }

}
