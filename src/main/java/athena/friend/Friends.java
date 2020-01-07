package athena.friend;

import athena.exception.EpicGamesErrorException;
import athena.friend.resource.Friend;
import athena.friend.resource.blocked.Blocked;
import athena.friend.resource.settings.FriendSettings;
import athena.friend.resource.summary.Profile;
import athena.friend.resource.summary.Summary;
import athena.friend.service.FriendsPublicService;
import athena.util.request.Requests;
import okhttp3.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public void add(String accountId) throws EpicGamesErrorException {
        final var call = service.add(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Removes a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void removeOrDecline(String accountId) throws EpicGamesErrorException {
        final var call = service.remove(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Blocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void block(String accountId) throws EpicGamesErrorException {
        final var call = service.block(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unblocks a friend via their account ID.
     *
     * @param accountId the ID of the account.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public void unblock(String accountId) throws EpicGamesErrorException {
        final var call = service.unblock(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Gets a list of all friends.
     *
     * @param includePending {@code true} if pending friend requests should be included.
     * @return a list of friends
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<Friend> friends(boolean includePending) throws EpicGamesErrorException {
        final var call = service.friends(localAccountId, includePending);
        return Requests.executeCall(call);
    }

    /**
     * Gets a list of all blocked friends.
     *
     * @return a list of all blocked friends (account IDs)
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public List<String> blocked() throws EpicGamesErrorException {
        final var call = service.blocked(localAccountId);
        return Requests.executeCall(call).stream().map(Blocked::accountId).collect(Collectors.toList());
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
        final var call = service.setAlias(localAccountId, accountId, RequestBody.create(alias, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
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
        final var call = service.remove(localAccountId, accountId);
        Requests.executeVoidCall(call);
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
        final var call = service.setNote(localAccountId, accountId, RequestBody.create(note, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
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
        final var call = service.remove(localAccountId, accountId);
        Requests.executeVoidCall(call);
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
     * @return a {@link Profile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Profile friendProfile(String accountId) throws EpicGamesErrorException {
        final var call = service.profile(localAccountId, accountId, true);
        return Requests.executeCall(call);
    }

    /**
     * Get a friend profile.
     *
     * @param friend the friend
     * @return a {@link Profile} that represents their profile.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Profile friendProfile(Friend friend) throws EpicGamesErrorException {
        return friendProfile(friend.accountId());
    }

    /**
     * Get the friend summary.
     *
     * @return a {@link Summary} representing the response.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public Summary summary() throws EpicGamesErrorException {
        final var call = service.summary(localAccountId, true);
        return Requests.executeCall(call);
    }

    /**
     * Get the current friend settings.
     *
     * @return a {@link FriendSettings} that represents the settings.
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings settings() throws EpicGamesErrorException {
        final var call = service.settings(localAccountId);
        return Requests.executeCall(call);
    }

    /**
     * Set the friend settings
     *
     * @param settings the settings
     * @throws EpicGamesErrorException if the API returned an error response.
     */
    public FriendSettings setSettings(FriendSettings settings) throws EpicGamesErrorException {
        final var call = service.setSettings(localAccountId, settings);
        return Requests.executeCall(call);
    }

    /**
     * @param accountId the account ID to check
     * @return {@code true} if the provided {@code accountId} is a friend.
     */
    public boolean isFriend(String accountId) {
        return friends != null && friends.containsKey(accountId);
    }

}
