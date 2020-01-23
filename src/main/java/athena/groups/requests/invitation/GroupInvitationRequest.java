package athena.groups.requests.invitation;

/**
 * A request used to invite another member.
 * TODO: Might need more to work
 */
public final class GroupInvitationRequest {

    /**
     * The invite message.
     */
    private String message;

    /**
     * Static factory method to create a new {@link GroupInvitationRequest}
     *
     * @param message the invite message
     * @return a new {@link GroupInvitationRequest}
     */
    public static GroupInvitationRequest create(String message) {
        return new GroupInvitationRequest(message);
    }

    private GroupInvitationRequest(String message) {
        this.message = message;
    }
}
