package athena.friend.resource;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.chat.FriendChat;
import athena.friend.Friends;
import athena.friend.resource.summary.Profile;
import athena.friend.resource.types.FriendDirection;
import athena.friend.resource.types.FriendStatus;
import athena.friend.service.FriendsPublicService;
import athena.util.json.hooks.PostDeserialize;
import athena.util.json.request.Request;
import athena.util.request.Requests;
import okhttp3.RequestBody;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.impl.JidCreate;

import java.time.Instant;

/**
 * Represents an Epic Games/Fortnite friend.
 */
public final class Friend {

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

    /**
     * The account
     */
    private Account account;

    /**
     * Bare JID of this friend.
     */
    private BareJid jid;

    /**
     * The friends public service.
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService friendsPublicService;
    /**
     * The local account
     */
    @Request(item = Account.class, local = true)
    private Account localAccount;

    /**
     * The accounts provider.
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    /**
     * The friends provider
     */
    @Request(item = Friends.class)
    private Friends friends;
    /**
     * Friend chat provider.
     */
    @Request(item = FriendChat.class)
    private FriendChat chat;

    @PostDeserialize
    private void postDeserialize() {
        jid = JidCreate.bareFromOrThrowUnchecked(accountId + "@prod.ol.epicgames.com");
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
        if (account == null) {
            account = accounts.findByAccountId(accountId);
        }
        return account;
    }

    /**
     * Remove this friend.
     */
    public void unfriend() {
        final var call = friendsPublicService.remove(localAccount.accountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Block this friend.
     */
    public void block() {
        final var call = friendsPublicService.block(localAccount.accountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Unblock this friend, if you just blocked them on accident I guess?
     */
    public void unblock() {
        final var call = friendsPublicService.unblock(localAccount.accountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Set the alias for this friend.
     *
     * @param alias the alias, minimum 3 characters maximum 15 characters.
     */
    public void setAlias(String alias) {
        if (alias.length() < 3 || alias.length() > 16) throw new IllegalArgumentException("Alias must be 3 characters minimum and 16 characters maximum.");
        final var call = friendsPublicService.setAlias(localAccount.accountId(), accountId, RequestBody.create(alias, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Remove the alias set for this friend
     */
    public void removeAlias() {
        final var call = friendsPublicService.removeAlias(localAccount.accountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Set the note for this friend.
     *
     * @param note the note, minimum 3 characters maximum 255 characters.
     */
    public void setNote(String note) {
        if (note.length() < 3 || note.length() > 255) throw new IllegalArgumentException("Note must be 3 characters minimum and 255 characters maximum.");
        final var call = friendsPublicService.setNote(localAccount.accountId(), accountId, RequestBody.create(note, FriendsPublicService.MEDIA_TYPE));
        Requests.executeVoidCall(call);
    }

    /**
     * Remove the note set for this friend.
     */
    public void removeNote() {
        final var call = friendsPublicService.removeNote(localAccount.accountId(), accountId);
        Requests.executeVoidCall(call);
    }

    /**
     * Retrieve the profile for this friend, this method is blocking.
     *
     * @return the {@link Profile} for this friend
     */
    public Profile profile() {
        final var call = friendsPublicService.profile(localAccount.accountId(), accountId, true);
        return Requests.executeCall(call);
    }

    /**
     * Send a message to this friend.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        chat.sendMessage(this, message);
    }

    /**
     * @return the jid of this friend.
     */
    public BareJid jid() {
        return jid;
    }
}
