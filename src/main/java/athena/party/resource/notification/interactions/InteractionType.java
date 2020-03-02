package athena.party.resource.notification.interactions;

/**
 * Represents an interaction type
 * TODO
 */
public enum InteractionType {

    /**
     * A ping has been sent.
     */
    PING_SENT("PingSent"),
    /**
     * A party invite has been sent.
     */
    PARTY_INVITE_SENT("PartyInviteSent");

    /**
     * The name of the interaction
     */
    private final String name;

    InteractionType(String name) {
        this.name = name;
    }

}
