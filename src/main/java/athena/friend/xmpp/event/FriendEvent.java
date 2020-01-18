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
public abstract class FriendEvent {

    protected final Friendship friendship;
    protected final FriendApiObject friendApiObject;
    protected final DefaultAthenaContext context;

    protected final String accountId, localAccountId;
    protected final Instant timestamp;
    protected final FriendType type;

    public FriendEvent(FriendApiObject notification, DefaultAthenaContext context) {
        this.friendApiObject = notification;
        this.friendship = null;
        this.context = context;

        this.accountId = notification.accountId();
        this.localAccountId = context.localAccountId();
        this.timestamp = notification.timestamp();
        this.type = FriendType.typeOf(notification.type());
    }

    public FriendEvent(Friendship notification, DefaultAthenaContext context) {
        this.friendship = notification;
        this.friendApiObject = null;
        this.context = context;

        this.accountId = notification.from();
        this.localAccountId = context.localAccountId();
        this.timestamp = notification.timestamp();
        this.type = FriendType.typeOf(notification.type());
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
        return type;
    }
}
