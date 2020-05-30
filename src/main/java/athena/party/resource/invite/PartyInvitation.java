package athena.party.resource.invite;

import athena.account.Accounts;
import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.party.resource.meta.invites.PingOrInvitationMeta;
import athena.types.Platform;
import athena.util.json.request.Request;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents a party invitation
 */
public final class PartyInvitation {

    /**
     * The ID of the party ID.
     */
    @SerializedName("party_id")
    private String partyId;

    /**
     * Who the invite was sent by.
     */
    @SerializedName("sent_by")
    private String sentBy;

    /**
     * The meta of this invitation
     */
    private PingOrInvitationMeta meta;

    /**
     * Who the invitation was sent to.
     */
    @SerializedName("sent_to")
    private String sentTo;

    /**
     * When the invite was sent.
     */
    @SerializedName("sent_at")
    private Instant sentAt;
    /**
     * When the invite was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;
    /**
     * When the invite expires.
     */
    @SerializedName("expires_at")
    private Instant expiresAt;
    /**
     * The status of this invite - "SENT"
     */
    private String status;

    /**
     * The local account
     */
    @Request(item = Account.class, local = true)
    private Account account;

    /**
     * The accounts provider
     */
    @Request(item = Accounts.class)
    private Accounts accounts;

    /**
     * The accounts service
     */
    @Request(item = AccountPublicService.class)
    private AccountPublicService accountPublicService;

    /**
     * The friends service
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService friendsPublicService;

    /**
     * @return the ID of the party
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return who it was sent by
     */
    public String sentByAccountId() {
        return sentBy;
    }

    /**
     * @return who it was sent by - their account.
     */
    public Account sentByAccount() {
        return Requests.executeCall(accountPublicService.findByDisplayName(displayName()));
    }

    /**
     * @return the friend profile of who sent this invite.
     */
    public Profile sentByProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), sentBy, true));
    }

    /**
     * @return the connection type of who sent the invite.
     */
    public String type() {
        return meta.type();
    }

    /**
     * @return the platform of who sent the invite
     */
    public Platform platform() {
        return meta.platform();
    }

    /**
     * @return the display name of who sent the invite
     */
    public String displayName() {
        return meta.displayName();
    }

    /**
     * @return the build ID.
     */
    public String buildId() {
        return meta.buildId();
    }

    /**
     * @return the platform data
     */
    public String platformData() {
        return meta.platformData();
    }

    /**
     * @return the account ID of who the invite was sent to.
     */
    public String sentTo() {
        return sentTo;
    }

    /**
     * @return the {@link Account} of who the invite was sent to.
     */
    public Account sentToAccount() {
        return accounts.findByAccountId(sentTo);
    }

    /**
     * @return the {@link Profile} of who the invite was sent to.
     * They must be a friend for this call to succeed.
     */
    public Profile sentToProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), sentTo, true));
    }

    /**
     * @return when this invite was sent
     */
    public Instant sentAt() {
        return sentAt;
    }

    /**
     * @return when this invite was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return when this invite expires.
     */
    public Instant expiresAt() {
        return expiresAt;
    }

    /**
     * @return the status of this invite, "SENT"/???
     */
    public String status() {
        return status;
    }

}
