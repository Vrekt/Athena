package athena.friend.xmpp.event.events;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.friend.xmpp.event.AbstractFriendEvent;
import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.util.json.request.Request;
import athena.util.request.Requests;

/**
 * An event that is invoked when a friend request is received.
 */
public final class FriendRequestEvent extends AbstractFriendEvent {

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
     * The friends service
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService friendsPublicService;

    public FriendRequestEvent(FriendApiObject friendApiObject, FriendType friendType) {
        super(friendApiObject, friendType);
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
     * Accept the friend request.
     *
     * @return their profile, or {@code null} if none was found.
     */
    public Profile accept() {
        Requests.executeVoidCall(friendsPublicService.add(account.accountId(), accountId));
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), accountId, true));
    }


    /**
     * Decline the friend request.
     */
    public void decline() {
        Requests.executeVoidCall(friendsPublicService.remove(account.accountId(), accountId));
    }

}
