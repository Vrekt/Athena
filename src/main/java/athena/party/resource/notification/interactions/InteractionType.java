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
    PARTY_INVITE_SENT("PartyInviteSent"),
    /**
     * The party has been joined
     */
    PARTY_JOINED("PartyJoined"),
    /**
     * The party has been left.
     */
    PARTY_LEFT("PartyLeft");

    /**
     * The name of the interaction
     */
    private final String name;

    InteractionType(String name) {
        this.name = name;
    }

}
