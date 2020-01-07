package athena.friend.resource.summary;

import athena.friend.resource.settings.FriendSettings;
import athena.friend.resource.summary.types.BasicFriend;

import java.util.List;

/**
 * Represents a summary response from {@link athena.friend.service.FriendsPublicService}
 */
public final class Summary {

    /**
     * List of friend summaries.
     */
    private List<Profile> friends;

    /**
     * List of incoming friends, outgoing suggested and the blocklist.
     */
    private List<BasicFriend> incoming, outgoing, suggested, blocklist;

    /**
     * Friend settings.
     */
    private FriendSettings settings;

    /**
     * @return list of friend summaries.
     */
    public List<Profile> friends() {
        return friends;
    }

    private Summary() {
    }

    /**
     * Get a profile that has the {@code accountId}
     *
     * @param accountId the account ID
     * @return the profile or {@code null} if not found.
     */
    public Profile profileFromAccountId(String accountId) {
        return friends.stream().filter(profile -> profile.accountId().equals(accountId)).findAny().orElse(null);
    }

    /**
     * Get a profile that has the {@code displayName}
     *
     * @param displayName the display name
     * @return the profile or {@code null} if not found.
     */
    public Profile profileFromDisplayName(String displayName) {
        return friends.stream().filter(profile -> profile.displayName().equals(displayName)).findAny().orElse(null);
    }

    /**
     * @return list of incoming friend requests.
     */
    public List<BasicFriend> incoming() {
        return incoming;
    }

    /**
     * @return list of outgoing friend requests.
     */
    public List<BasicFriend> outgoing() {
        return outgoing;
    }

    /**
     * @return list of suggested friends.
     */
    public List<BasicFriend> suggested() {
        return suggested;
    }

    /**
     * @return the blocklist.
     */
    public List<BasicFriend> blocklist() {
        return blocklist;
    }

    /**
     * @return the friend settings.
     */
    public FriendSettings settings() {
        return settings;
    }

    /**
     * @return the accept invites setting.
     */
    public String acceptInvites() {
        return settings.acceptInvites();
    }

}
