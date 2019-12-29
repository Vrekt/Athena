package athena.friend.resource.profile;

import com.google.gson.annotations.Expose;

import java.time.Instant;

/**
 * Represents a friend profile.
 */
public final class FriendProfile {

    /**
     * Account ID, their alias and note.
     */
    private String accountId, alias, note;

    /**
     * Groups? Currently un-implemented/unknown.
     * TODO:
     */
    @Expose(deserialize = false)
    private String[] groups;

    /**
     * Number of mutual friends.
     */
    private int mutual;

    /**
     * If this friend is a favorite currently un-implemented/unknown
     * TODO:
     */
    private boolean favorite;

    /**
     * When this friend was created.
     */
    private Instant created;

    /**
     * @return the account ID of this friend.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the alias set for this friend.
     */
    public String alias() {
        return alias;
    }

    /**
     * @return the note set for this friend.
     */
    public String note() {
        return note;
    }

    /**
     * @return a set of groups.
     */
    public String[] groups() {
        return groups;
    }

    /**
     * @return number of mutual friends.
     */
    public int mutual() {
        return mutual;
    }

    /**
     * @return if this friend is a favorite.
     */
    public boolean favorite() {
        return favorite;
    }

    /**
     * @return when the friend/profile was created.
     */
    public Instant created() {
        return created;
    }
}
