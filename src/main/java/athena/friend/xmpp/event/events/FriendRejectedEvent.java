package athena.friend.xmpp.event.events;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.Friendship;
import athena.util.json.request.Request;

/**
 * An event for when somebody rejects the friend request.
 */
public final class FriendRejectedEvent extends AbstractFriendEvent {

    /**
     * The accounts provider.
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    public FriendRejectedEvent(Friendship friendship, FriendType friendType) {
        super(friendship, friendType);
    }

    /**
     * Retrieve the account of this friend request.
     *
     * @return their account.
     */
    public Account account() {
        return accounts.findByAccountId(accountId);
    }

}
