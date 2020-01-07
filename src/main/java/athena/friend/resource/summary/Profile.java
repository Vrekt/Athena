package athena.friend.resource.summary;

import athena.account.resource.Account;
import athena.exception.EpicGamesErrorException;
import athena.friend.service.FriendsPublicService;
import athena.util.json.PostProcessable;
import athena.util.request.Requests;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import okhttp3.RequestBody;

import java.time.Instant;
import java.util.Map;

/**
 * Represents a friend profile.
 */
public final class Profile extends PostProcessable {

    /**
     * Account ID, their alias and note.
     */
    private String accountId, alias, note, displayName;

    /**
     * Groups? Currently un-implemented/unknown.
     * TODO:
     */
    @Expose(deserialize = false)
    private String[] groups;

    /**
     * Map of connections.
     * ex: key=xbl value=xxx
     */
    private Map<String, JsonObject> connections;

    /**
     * Number of mutual friends.
     */
    private int mutual;

    /**
     * If this friend is a favorite currently un-implemented/unknown
     * TODO:
     */
    private boolean favorite;

    /**
     * When this friend was created.
     */
    private Instant created;

    private Profile() {
    }

    /**
     * @return the account ID of this friend.
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return the display-name of this friend, only present if query was set to {@code true} in the original request.
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return the alias set for this friend.
     */
    public String alias() {
        return alias;
    }

    /**
     * @return the note set for this friend.
     */
    public String note() {
        return note;
    }

    /**
     * @return a set of groups.
     */
    public String[] groups() {
        return groups;
    }

    /**
     * @return map of connections
     * ex: key=xbl value=xxx
     */
    public Map<String, JsonObject> connections() {
        return connections;
    }

    /**
     * @return number of mutual friends.
     */
    public int mutual() {
        return mutual;
    }

    /**
     * @return if this friend is a favorite.
     */
    public boolean favorite() {
        return favorite;
    }

    /**
     * @return when the friend/profile was created.
     */
    public Instant created() {
        return created;
    }

    /**
     * Gets the account for this friend.
     * This method is blocking.
     *
     * @return the account of this friend.
     */
    public Account account() {
        final var call = accountPublicService.findOneByAccountId(accountId);
        final var result = Requests.executeCall(call);
        if (result.length == 0) throw EpicGamesErrorException.create("Cannot find account " + accountId);
        return result[0];
    }

    /**
     * Remove this friend.
     */
    public void unfriend() {
        final var call = friendsPublicService.remove(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Block this friend.
     */
    public void block() {
        final var call = friendsPublicService.block(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unblock this friend, if you just blocked them on accident I guess?
     */
    public void unblock() {
        final var call = friendsPublicService.unblock(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Set the alias for this friend.
     *
     * @param alias the alias, minimum 3 characters maximum 15 characters.
     */
    public void setAlias(String alias) {
        if (alias.length() < 3 || alias.length() > 16) throw new IllegalArgumentException("Alias must be 3 characters minimum and 16 characters maximum.");
        final var call = friendsPublicService.setAlias(localAccountId, accountId, RequestBody.create(alias, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Remove the alias set for this friend
     */
    public void removeAlias() {
        final var call = friendsPublicService.removeAlias(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Set the note for this friend.
     *
     * @param note the note, minimum 3 characters maximum 255 characters.
     */
    public void setNote(String note) {
        if (note.length() < 3 || note.length() > 255) throw new IllegalArgumentException("Note must be 3 characters minimum and 255 characters maximum.");
        final var call = friendsPublicService.setNote(localAccountId, accountId, RequestBody.create(note, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Remove the note set for this friend.
     */
    public void removeNote() {
        final var call = friendsPublicService.removeNote(localAccountId, accountId);
        Requests.executeVoidCall(call);
    }

}
