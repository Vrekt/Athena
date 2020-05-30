package athena.friend.xmpp.event;

import athena.friend.xmpp.type.FriendType;
import athena.friend.xmpp.types.friend.FriendApiObject;
import athena.friend.xmpp.types.friend.Friendship;

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

    public AbstractFriendEvent(Friendship friendship, FriendType friendType) {
        this.friendship = friendship;
        this.friendApiObject = null;
        this.friendType = friendType;

        this.accountId = friendship.from();
        this.timestamp = friendship.timestamp();
    }

    public AbstractFriendEvent(FriendApiObject friendApiObject, FriendType friendType) {
        this.friendApiObject = friendApiObject;
        this.friendship = null;
        this.friendType = friendType;

        this.accountId = friendApiObject.accountId();
        this.timestamp = friendApiObject.timestamp();
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
