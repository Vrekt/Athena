package athena.party.resource.ping;

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
 * Represents a party ping.
 */
public final class PartyPing {

    /**
     * Who the ping was sent by.
     */
    @SerializedName("sent_by")
    private String sentBy;

    /**
     * Who the ping was sent to.
     */
    @SerializedName("sent_to")
    private String sentTo;

    /**
     * When the ping was sent.
     */
    @SerializedName("sent_at")
    private Instant sentAt;

    /**
     * When the ping expires.
     */
    @SerializedName("expires_at")
    private Instant expiresAt;

    /**
     * The meta of this ping
     */
    private PingOrInvitationMeta meta;

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
     * @return the friend profile of who sent this ping.
     */
    public Profile sentByProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), sentBy, true));
    }

    /**
     * @return the connection type of who sent the ping.
     */
    public String type() {
        return meta.type();
    }

    /**
     * @return the platform of who sent the ping.
     */
    public Platform platform() {
        return meta.platform();
    }

    /**
     * @return the display name of who sent the ping
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
     * @return the account ID of who the ping was sent to.
     */
    public String sentTo() {
        return sentTo;
    }

    /**
     * @return the {@link Account} of who the ping was sent to.
     */
    public Account sentToAccount() {
        return accounts.findByAccountId(sentTo);
    }

    /**
     * @return the {@link Profile} of who the ping was sent to.
     * They must be a friend for this call to succeed.
     */
    public Profile sentToProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), sentTo, true));
    }

    /**
     * @return when this ping was sent
     */
    public Instant sentAt() {
        return sentAt;
    }

    /**
     * @return when this ping expires.
     */
    public Instant expiresAt() {
        return expiresAt;
    }

}
