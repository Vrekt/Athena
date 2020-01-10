package athena.friend.xmpp.notification.type;

import java.util.List;

/**
 * Represents a friend notification type.
 */
public enum FNotificationType {

    FRIENDSHIP_REQUEST("FRIENDSHIP_REQUEST"),
    FRIENDSHIP_REMOVE("FRIENDSHIP_REMOVE"),
    FRIEND_REMOVAL("com.epicgames.friends.core.apiobjects.FriendRemoval"),
    FRIEND("com.epicgames.friends.core.apiobjects.Friend"),
    UNKNOWN("");

    private static final List<FNotificationType> TYPES = List.of(values());
    private final String type;

    FNotificationType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    /**
     * Find the type.
     *
     * @param type the type
     * @return the notification type.
     */
    public static FNotificationType typeOf(String type) {
        return TYPES.stream().filter(fNotificationType -> fNotificationType.type.equalsIgnoreCase(type)).findAny().orElse(UNKNOWN);
    }

}
