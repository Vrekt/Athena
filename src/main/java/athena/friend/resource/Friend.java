package athena.friend.resource;

import athena.account.resource.Account;
import athena.friend.resource.profile.FriendProfile;
import athena.friend.resource.types.FriendDirection;
import athena.friend.resource.types.FriendStatus;
import athena.util.json.PostProcessable;
import athena.util.request.Requests;

import java.time.Instant;
import java.util.Optional;

/**
 * Represents an Epic Games/Fortnite friend.
 */
public final class Friend extends PostProcessable {

    /**
     * The account ID.
     */
    private String accountId;
    /**
     * The status of this friend (accepted or pending)
     */
    private FriendStatus status;
    /**
     * The direction of this friend
     */
    private FriendDirection direction;
    /**
     * When this friend was created. (sent?)
     */
    private Instant created;
    /**
     * If this friend is a favorite.
     */
    private boolean favorite;

    private Friend() {

    }

    /**
     * @return the account ID of this friend.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the status of this friend, (pending, accepted)
     */
    public FriendStatus status() {
        return status;
    }

    /**
     * @return the direction this friend was sent in, (outbound = you sent, inbound = they sent).
     */
    public FriendDirection direction() {
        return direction;
    }

    /**
     * @return when this friend was created (the request was sent/accepted?)
     */
    public Instant created() {
        return created;
    }

    /**
     * @return {@code false} always, seems to be un-implemented.
     */
    public boolean favorite() {
        return favorite;
    }

    /**
     * Gets the account for this friend.
     * This method is blocking.
     *
     * @return the account of this friend.
     */
    public Optional<Account> account() {
        final var call = accountPublicService.findOneByAccountId(accountId);
        final var result = Requests.executeCall("Failed to find account for " + accountId, call);
        return result.length == 0 ? Optional.empty() : Optional.of(result[0]); // return an empty account if not found.
    }

    /**
     * Remove this friend.
     */
    public void removeFriend() {
        final var call = friendsPublicService.removeFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove friend " + accountId, call);
    }

    /**
     * Block this friend.
     */
    public void block() {
        final var call = friendsPublicService.blockFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to block account " + accountId + ".", call);
    }

    /**
     * Unblock this friend, if you just blocked them on accident I guess?
     */
    public void unblock() {
        final var call = friendsPublicService.unblockFriendByAccountId(localAccountId, accountId);
        Requests.executeVoidCall("Failed to unblock account " + accountId, call);
    }

    /**
     * Set the alias for this friend.
     *
     * @param alias the alias, minimum 3 characters maximum 15 characters.
     */
    public void setAlias(String alias) {
        if (alias.length() < 3 || alias.length() > 16) throw new IllegalArgumentException("Alias must be 3 characters minimum and 16 characters maximum.");
        final var call = friendsPublicService.setFriendAlias(localAccountId, accountId, alias);
        Requests.executeVoidCall("Failed to set alias for friend " + accountId, call);
    }

    /**
     * Remove the alias set for this friend
     */
    public void removeAlias() {
        final var call = friendsPublicService.removeFriendAlias(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove alias for friend " + accountId, call);
    }

    /**
     * Set the note for this friend.
     *
     * @param note the note, minimum 3 characters maximum 255 characters.
     */
    public void setNote(String note) {
        if (note.length() < 3 || note.length() > 255) throw new IllegalArgumentException("Note must be 3 characters minimum and 255 characters maximum.");
        final var call = friendsPublicService.setFriendNote(localAccountId, accountId, note);
        Requests.executeVoidCall("Failed to set a note for friend " + accountId, call);
    }

    /**
     * Remove the note set for this friend.
     */
    public void removeNote() {
        final var call = friendsPublicService.removeFriendNote(localAccountId, accountId);
        Requests.executeVoidCall("Failed to remove a note for friend " + accountId, call);
    }

    /**
     * Retrieve the profile for this friend, this method is blocking.
     *
     * @return the {@link FriendProfile} for this friend
     */
    public FriendProfile profile() {
        final var call = friendsPublicService.friendProfile(localAccountId, accountId);
        return Requests.executeCall("Failed to get profile for friend " + accountId, call);
    }

}
