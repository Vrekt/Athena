package athena.friend.xmpp.event.events;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.Friendship;
import athena.util.json.request.Request;
import athena.util.request.Requests;

/**
 * An event for when a friend request is accepted.
 */
public final class FriendAcceptedEvent extends AbstractFriendEvent {

    /**
     * The local account
     */
    @Request(item = Account.class, local = true)
    private Account account;

    /**
     * The accounts provider.
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    /**
     * The friends public service
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService service;

    public FriendAcceptedEvent(Friendship friendship, FriendType friendType) {
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

    /**
     * @return their profile
     */
    public Profile profile() {
        return Requests.executeCall(service.profile(account.accountId(), accountId, true));
    }

}
