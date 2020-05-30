package athena.friend.xmpp.event.events;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.util.json.request.Request;

/**
 * An event for when a friend is deleted.
 */
public final class FriendDeletedEvent extends AbstractFriendEvent {

    /**
     * The accounts provider.
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    public FriendDeletedEvent(FriendApiObject friendApiObject, FriendType friendType) {
        super(friendApiObject, friendType);
    }

    /**
     * Retrieve the account of this event.
     *
     * @return their account.
     */
    public Account account() {
        return accounts.findByAccountId(accountId);
    }

}
