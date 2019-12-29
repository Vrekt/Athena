package athena.friend;

import athena.exception.EpicGamesErrorException;
import athena.friend.resource.Friend;
import athena.friend.resource.profile.FriendProfile;
import athena.friend.resource.settings.FriendSettings;
import athena.friend.service.FriendsPublicService;
import athena.util.request.Requests;

import java.util.List;
import java.util.Map;

/**
 * Provides easy access to the {@link FriendsPublicService}
 */
public final class Friends {

    /**
     * The service that handles the requests.
     */
    private final FriendsPublicService service;
    /**
     * The account ID of the athena instance this came from.
     */
    private final String localAccountId;

    /**
     * A list of all friends.
     * TODO: Get all friends on login and add them to this list?
     * TODO: Add friends to this list as they come in.
     */
    private Map<String, Friend> friends;

    public Friends(FriendsPublicService friendsPublicService, String localAccountId) {
        this.service = friendsPublicService;
        this.localAccountId = localAccountId;
    }

    /**
     * @return the service that is tied to this class.
     */
    public FriendsPublicService service() {
        return service;
    }

    /**
     * Adds a friend via their account ID.
     *
     * @param accountId the ID of the account
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void addFriendByAccountId(String accountId) throws EpicGamesErrorException {
        final var call = service.addFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to add account " + accountId + " as a friend.", call);
    }

    /**
     * Removes a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeOrDeclineFriendByAccountId(String accountId) throws EpicGamesErrorException {
        final var call = service.removeFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove/decline account " + accountId, call);
    }

    /**
     * Blocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void blockFriendByAccountId(String accountId) throws EpicGamesErrorException {
        final var call = service.blockFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to block account " + accountId, call);
    }

    /**
     * Unblocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void unblockFriendByAccountId(String accountId) throws EpicGamesErrorException {
        final var call = service.unblockFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to unblock account " + accountId, call);
    }

    /**
     * Gets a list of all friends.
     *
     * @param includePending {@code true} if pending friend requests should be included.
     * @return a list of friends
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Friend> getAllFriends(boolean includePending) throws EpicGamesErrorException {
        final var call = service.friends(localAccountId, includePending);
        return Requests.executeCallOptional("Failed to get all friends for account " + localAccountId, call).orElse(List.of());
    }

    /**
     * Gets a list of all blocked friends.
     * TODO:
     *
     * @return a list of all blocked friends
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Friend> getAllBlocked() throws EpicGamesErrorException {
        final var call = service.blocked(localAccountId);
        return List.of(); // TODO
    }

    /**
     * Sets the alias (nickname) of a friend.
     *
     * @param accountId the account ID of the friend
     * @param alias     the alias (nickname) to put.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendAlias(String accountId, String alias) throws EpicGamesErrorException {
        if (alias.length() < 3 || alias.length() > 16) throw new IllegalArgumentException("Alias must be 3 characters minimum and 16 characters maximum.");
        final var call = service.setFriendAlias(localAccountId, accountId, alias);
        Requests.executeVoidCall("Failed to set alias for friend " + accountId, call);
    }

    /**
     * Sets the alias (nickname) of a friend.
     *
     * @param friend the friend
     * @param alias  the alias (nickname) to put.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendAlias(Friend friend, String alias) throws EpicGamesErrorException {
        setFriendAlias(friend.accountId(), alias);
    }

    /**
     * Removes the alias of a friend.
     *
     * @param accountId the account ID of the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendAlias(String accountId) throws EpicGamesErrorException {
        final var call = service.removeFriendAlias(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove alias for friend " + accountId, call);
    }

    /**
     * Removes the alias of a friend.
     *
     * @param friend the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendAlias(Friend friend) throws EpicGamesErrorException {
        removeFriendAlias(friend.accountId());
    }

    /**
     * Sets the note of a friend.
     *
     * @param accountId the account ID of the friend
     * @param note      the note
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendNote(String accountId, String note) throws EpicGamesErrorException {
        if (note.length() < 3 || note.length() > 255) throw new IllegalArgumentException("Note must be 3 characters minimum and 255 characters maximum.");
        final var call = service.setFriendNote(localAccountId, accountId, note);
        Requests.executeVoidCall("Failed to set note for friend " + accountId, call);
    }

    /**
     * Sets a friend note
     *
     * @param friend the friend
     * @param note   the note
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void setFriendNote(Friend friend, String note) throws EpicGamesErrorException {
        setFriendNote(friend.accountId(), note);
    }

    /**
     * Removes a friend note
     *
     * @param accountId the account ID of the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendNote(String accountId) throws EpicGamesErrorException {
        final var call = service.removeFriendNote(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove note for friend " + accountId, call);
    }

    /**
     * Removes a friend note
     *
     * @param friend the friend
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeFriendNote(Friend friend) throws EpicGamesErrorException {
        removeFriendNote(friend.accountId());
    }

    /**
     * Get a friend profile.
     *
     * @param accountId the account ID of the friend.
     * @return a {@link FriendProfile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendProfile getFriendProfile(String accountId) throws EpicGamesErrorException {
        final var call = service.friendProfile(localAccountId, accountId);
        return Requests.executeCall("Failed to retrieve profile for " + accountId, call);
    }

    /**
     * Get a friend profile.
     *
     * @param friend the friend
     * @return a {@link FriendProfile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendProfile getFriendProfile(Friend friend) throws EpicGamesErrorException {
        return getFriendProfile(friend.accountId());
    }

    /**
     * Get the current friend settings.
     *
     * @return a {@link FriendSettings} that represents the settings.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings getSettings() throws EpicGamesErrorException {
        final var call = service.settings(localAccountId);
        return Requests.executeCall("Failed to retrieve settings", call);
    }

    /**
     * Set the friend settings
     *
     * @param settings the settings
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings setSettings(FriendSettings settings) throws EpicGamesErrorException {
        final var call = service.setSettings(localAccountId, settings);
        return Requests.executeCall("Failed to set settings", call);
    }

    /**
     * @param accountId the account ID to check
     * @return {@code true} if the provided {@code accountId} is a friend.
     */
    public boolean isFriend(String accountId) {
        return friends != null && friends.containsKey(accountId);
    }

}
