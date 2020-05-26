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
     * Represents a member joining.
     */
    MEMBER_JOINED("com.epicgames.social.party.notification.v0.MEMBER_JOINED"),
    /**
     * Represents a member leaving.
     */
    MEMBER_LEFT("com.epicgames.social.party.notification.v0.MEMBER_LEFT"),
    /**
     * Represents when a member updates their metadata.
     */
    MEMBER_STATE_UPDATED("com.epicgames.social.party.notification.v0.MEMBER_STATE_UPDATED"),
    /**
     * Represents when a member gets promoted.
     */
    MEMBER_NEW_CAPTAIN("com.epicgames.social.party.notification.v0.MEMBER_NEW_CAPTAIN"),
    /**
     * Represents when a member gets kicked.
     */
    MEMBER_KICKED("com.epicgames.social.party.notification.v0.MEMBER_KICKED"),
    /**
     * Represents when a member disconnects from the party by closing their game.
     */
    MEMBER_DISCONNECTED("com.epicgames.social.party.notification.v0.MEMBER_DISCONNECTED"),
    /**
     * Represents when a member expires after disconnecting.
     */
    MEMBER_EXPIRED("com.epicgames.social.party.notification.v0.MEMBER_EXPIRED"),
    /**
     * Represents when a member requires confirmation to join.
     */
    MEMBER_REQUIRE_CONFIRMATION("com.epicgames.social.party.notification.v0.MEMBER_REQUIRE_CONFIRMATION"),

    /**
     * Represents when the party gets updated
     */
    PARTY_UPDATED("com.epicgames.social.party.notification.v0.PARTY_UPDATED"),

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
