package athena.friend.xmpp.type;

import java.util.List;

/**
 * Represents a friend types type.
 */
public enum FriendType {

    /**
     * Indicates an entry was added to the blocklist.
     */
    BLOCK_LIST_ENTRY_ADDED("com.epicgames.friends.core.apiobjects.BlockListEntryAdded"),
    /**
     * Indicates an entry was removed from the blocklist.
     */
    BLOCK_LIST_ENTRY_REMOVED("com.epicgames.friends.core.apiobjects.BlockListEntryRemoved"),
    /**
     * Indicates a blocklist update.
     */
    USER_BLOCKLIST_UPDATE("USER_BLOCKLIST_UPDATE"),
    /**
     * Indicates a friendship request.
     */
    FRIENDSHIP_REQUEST("FRIENDSHIP_REQUEST"),
    /**
     * Indicates a friendship remove.
     */
    FRIENDSHIP_REMOVE("FRIENDSHIP_REMOVE"),
    /**
     * Indicates a friendship remove.
     */
    FRIEND_REMOVAL("com.epicgames.friends.core.apiobjects.FriendRemoval"),
    /**
     * Indicates a friendship action
     */
    FRIEND("com.epicgames.friends.core.apiobjects.Friend"),
    /**
     * Default.
     */
    UNKNOWN("UNKNOWN");

    /**
     * List of types.
     */
    private static final List<FriendType> TYPES = List.of(values());
    /**
     * The type name.
     */
    private final String type;

    FriendType(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }

    /**
     * Find the type.
     *
     * @param type the type
     * @return the types type.
     */
    public static FriendType typeOf(String type) {
        return TYPES.stream().filter(friendType -> friendType.type.equalsIgnoreCase(type)).findAny().orElse(UNKNOWN);
    }

}
