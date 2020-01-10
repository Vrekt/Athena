package athena.friend.xmpp.event;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.exception.EpicGamesErrorException;
import athena.friend.xmpp.notification.FriendType;
import athena.friend.xmpp.notification.Friendship;
import athena.friend.xmpp.notification.type.FNotificationType;
import athena.util.request.Requests;

import java.time.Instant;

/**
 * Used as a base for all friend events.
 */
public abstract class FriendEvent {

    protected final Friendship friendship;
    protected final FriendType friendType;
    protected final AthenaContext context;

    protected final String accountId, localAccountId;
    protected final Instant timestamp;
    protected final FNotificationType type;

    public FriendEvent(FriendType notification, AthenaContext context) {
        this.friendType = notification;
        this.friendship = null;
        this.context = context;

        this.accountId = notification.accountId();
        this.localAccountId = context.accountId();
        this.timestamp = notification.timestamp();
        this.type = FNotificationType.typeOf(notification.type());
    }

    public FriendEvent(Friendship notification, AthenaContext context) {
        this.friendship = notification;
        this.friendType = null;
        this.context = context;

        this.accountId = notification.from();
        this.localAccountId = context.accountId();
        this.timestamp = notification.timestamp();
        this.type = FNotificationType.typeOf(notification.type());
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
     * @return the friendship object, or {@code null} if this event was initialized with {@link FriendType}
     */
    public Friendship friendship() {
        return friendship;
    }

    /**
     * @return the friend-type object, or {@code null} if this event was initialized with {@link Friendship}
     */
    public FriendType friendType() {
        return friendType;
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
     * @return the notification type
     */
    public FNotificationType type() {
        return type;
    }
}
