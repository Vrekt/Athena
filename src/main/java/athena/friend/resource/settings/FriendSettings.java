package athena.friend.resource.settings;

/**
 * Represents friend settings.
 * Currently only one field known right now "acceptInvites"
 */
public final class FriendSettings {

    /**
     * "public" or "private"
     */
    private String acceptInvites;

    /**
     * @param acceptInvites either "public" or "private"
     */
    public FriendSettings(String acceptInvites) {
        this.acceptInvites = acceptInvites;
    }

    /**
     * @return the accept invites value.
     */
    public String acceptInvites() {
        return acceptInvites;
    }
}
