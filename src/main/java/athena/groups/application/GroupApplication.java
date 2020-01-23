package athena.groups.application;

import java.time.Instant;

/**
 * Represents a group application.
 */
public final class GroupApplication {

    /**
     * The account ID of who applied.
     * The group ID
     * the namespace, "kairos"
     * the name of the group.
     */
    private String accountId, groupId, namespace, name;
    /**
     * When the application was sent.
     */
    private Instant sentAt;

    private GroupApplication() {}

    /**
     * @return The account ID of who applied.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * Not always present if this object is a response from submitting an application.
     *
     * @return the group ID.
     */
    public String groupId() {
        return groupId;
    }

    /**
     * Not always present if this object is a response from submitting an application.
     *
     * @return the namespace
     */
    public String namespace() {
        return namespace;
    }

    /**
     * Not always present if this object is a response from submitting an application.
     *
     * @return the name of the group, or {@code null}
     */
    public String name() {
        return name;
    }

    /**
     * @return When the application was sent.
     */
    public Instant sentAt() {
        return sentAt;
    }
}
