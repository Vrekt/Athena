package athena.friend.xmpp.event;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.context.DefaultAthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.friend.xmpp.types.friend.Friendship;
import athena.friend.xmpp.type.FriendType;
import athena.util.request.Requests;

import java.time.Instant;

/**
 * Used as a base for all friend events.
 */
public abstract class AbstractFriendEvent {


    /**
     * The {@link Friendship} object.
     */
    protected final Friendship friendship;
    /**
     * The {@link FriendApiObject} object.
     */
    protected final FriendApiObject friendApiObject;
    /**
     * The athena context.
     */
    protected final DefaultAthenaContext context;

    /**
     * Account ID of this event.
     */
    protected final String accountId;
    /**
     * Timestamp of the event.
     */
    protected final Instant timestamp;
    /**
     * The friend-type.
     */
    protected final FriendType friendType;

    public AbstractFriendEvent(Friendship friendship, FriendType friendType, DefaultAthenaContext context) {
        this.friendship = friendship;
        this.friendApiObject = null;
        this.friendType = friendType;
        this.context = context;

        this.accountId = friendship.from();
        this.timestamp = friendship.timestamp();
    }

    public AbstractFriendEvent(FriendApiObject friendApiObject, FriendType friendType, DefaultAthenaContext context) {
        this.friendApiObject = friendApiObject;
        this.friendship = null;
        this.friendType = friendType;
        this.context = context;

        this.accountId = friendApiObject.accountId();
        this.timestamp = friendApiObject.timestamp();
    }

    /**
     * Retrieve the account of this friend request.
     *
     * @return their account.
     */
    public Account account() {
        final var call = context.account().findOneByAccountId(accountId);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Cannot find account " + accountId);
        return result[0];
    }

    /**
     * @return the friendship object, or {@code null} if this event was initialized with {@link FriendApiObject}
     */
    public Friendship friendship() {
        return friendship;
    }

    /**
     * @return the friend-type object, or {@code null} if this event was initialized with {@link Friendship}
     */
    public FriendApiObject friendType() {
        return friendApiObject;
    }

    /**
     * @return the athena context.
     */
    public AthenaContext context() {
        return context;
    }

    /**
     * @return the account ID.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the timestamp.
     */
    public Instant timestamp() {
        return timestamp;
    }

    /**
     * @return the types type
     */
    public FriendType type() {
        return friendType;
    }
}
