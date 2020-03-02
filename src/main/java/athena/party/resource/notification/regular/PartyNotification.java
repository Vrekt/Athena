package athena.party.resource.notification.regular;

import java.util.List;

/**
 * Represents a party notification.
 */
public enum PartyNotification {

    /**
     * Represents a ping.
     */
    PING("com.epicgames.social.party.notification.v0.PING"),
    /**
     * Represents an invite.
     */
    INITIAL_INVITE("com.epicgames.social.party.notification.v0.INITIAL_INVITE"),
    /**
     * An unknown notification.
     */
    UNKNOWN("UNKNOWN");

    /**
     * List of all notifications.
     */
    private static final List<PartyNotification> VALUES = List.of(values());
    /**
     * The type.
     */
    private final String type;

    PartyNotification(String type) {
        this.type = type;
    }

    /**
     * Get the {@link PartyNotification} from the provided {@code type}
     *
     * @param type the type
     * @return the party notification.
     */
    public static PartyNotification of(String type) {
        return VALUES.stream().filter(partyNotification -> partyNotification.type.equals(type)).findAny().orElse(UNKNOWN);
    }

}
