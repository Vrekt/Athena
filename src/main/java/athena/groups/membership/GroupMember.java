package athena.groups.membership;

import java.time.Instant;

/**
 * Represents a group member.
 */
public final class GroupMember {

    /**
     * The account ID of the member.
     */
    private String accountId;

    /**
     * If the member is an admin or owner.
     */
    private boolean admin, isOwner;

    /**
     * When this member joined the group.
     */
    private Instant joinedAt;

    /**
     * @return the account ID of this member.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return {@code true} if this member is an admin.
     */
    public boolean admin() {
        return admin;
    }

    /**
     * @return {@code true} if this member is an owner.
     */
    public boolean isOwner() {
        return isOwner;
    }

    /**
     * @return when this member joined the group.
     */
    public Instant joinedAt() {
        return joinedAt;
    }
}
