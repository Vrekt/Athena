package athena.groups.invitation;

import java.time.Instant;

/**
 * Represents a group invitation.
 */
public final class GroupInvitation {

    /**
     * Account ID of who the invite is to.
     * The group host account ID
     * The join message if any.
     * the group ID
     * the namespace, "kairos"
     * the name of the group
     */
    private String accountId, groupHost, message, groupId, namespace, name;

    /**
     * When the invite was sent.
     */
    private Instant sentAt;

    private GroupInvitation() {
    }

    /**
     * @return Account ID of who the invite is to.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return The group host account ID
     */
    public String groupHost() {
        return groupHost;
    }

    /**
     * @return the join message, or {@code null}
     */
    public String message() {
        return message;
    }

    /**
     * @return the group ID.
     */
    public String groupId() {
        return groupId;
    }

    /**
     * @return the namespace
     */
    public String namespace() {
        return namespace;
    }

    /**
     * @return the name of the group invited to
     */
    public String name() {
        return name;
    }

    /**
     * @return when the invite was sent.
     */
    public Instant sentAt() {
        return sentAt;
    }
}
