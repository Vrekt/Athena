package athena.channels.resource.channel.member;

/**
 * Represents a member inside a {@link athena.channels.resource.channel.Channel}
 */
public final class ChannelMember {

    /**
     * The account ID, displayName and status of this member.
     * NOTE: Status is not always given, only when querying for a {@link athena.channels.resource.channel.Channel},
     * other times it will be {@code null}
     */
    private String accountId, displayName, status;

    /**
     * @return the account ID of this member.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display name of this member.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return the status of this member, or {@code null}
     */
    public String status() {
        return status;
    }
}
