package athena.friend.resource;

import athena.Athena;
import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.adapter.ObjectJsonAdapter;
import athena.friend.resource.profile.FriendProfile;
import athena.friend.resource.types.FriendDirection;
import athena.friend.resource.types.FriendStatus;
import athena.friend.service.FriendsPublicService;
import athena.util.request.Requests;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.time.Instant;

/**
 * Represents an Epic Games/Fortnite friend.
 */
public final class Friend {

    /**
     * @return a new adapter for this object.
     */
    public static ObjectJsonAdapter<Friend> newAdapter() {
        return new Adapter();
    }

    /**
     * The account ID.
     */
    private final String accountId;
    /**
     * The status of this friend (accepted or pending)
     */
    private final FriendStatus status;
    /**
     * The direction of this friend
     */
    private final FriendDirection direction;
    /**
     * When this friend was created. (sent?)
     */
    private final Instant created;
    /**
     * If this friend is a favorite.
     */
    private final boolean favorite;

    /**
     * Account ID of the athena instance this friend belongs to.
     */
    private String localAccountId;

    /**
     * Services used by this class.
     */
    private FriendsPublicService friendsPublicService;
    private AccountPublicService accountPublicService;

    private Friend(String accountId, FriendStatus status, FriendDirection direction, Instant created, boolean favorite,
                   FriendsPublicService friendsPublicService, AccountPublicService accountPublicService, String localAccountId) {
        this.accountId = accountId;
        this.status = status;
        this.direction = direction;
        this.created = created;
        this.favorite = favorite;
        this.friendsPublicService = friendsPublicService;
        this.accountPublicService = accountPublicService;
        this.localAccountId = localAccountId;
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
    public Account account() {
        final var call = accountPublicService.findOneByAccountId(accountId);
        return Requests.executeCall("Failed to find account for " + accountId, call);
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

    /**
     * The adapter for friends
     */
    private static final class Adapter implements ObjectJsonAdapter<Friend> {

        private FriendsPublicService friendsPublicService;
        private AccountPublicService accountPublicService;
        private String localAccountId;

        @Override
        public Friend deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            final var object = json.getAsJsonObject();
            final var id = object.get("accountId").getAsString();
            final var status = object.get("status").getAsString();
            final var direction = object.get("direction").getAsString();
            final var created = object.get("created").getAsString();
            final var favorite = object.get("favorite").getAsBoolean();
            return new Friend(id, FriendStatus.valueOf(status), FriendDirection.valueOf(direction), Instant.parse(created), favorite,
                    friendsPublicService, accountPublicService, localAccountId);
        }

        @Override
        public void initialize(Athena athena) {
            this.friendsPublicService = athena.friendsPublicService();
            this.accountPublicService = athena.accountPublicService();
            this.localAccountId = athena.accountId();
        }
    }
}
