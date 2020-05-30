package athena.party.xmpp.event.member;

import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.party.Parties;
import athena.party.resource.Party;
import athena.party.resource.connection.Connection;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.party.service.PartyService;
import athena.util.json.request.Request;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * An event for when a member requires confirmation to join.
 */
public final class PartyMemberRequireConfirmationEvent {

    /**
     * When this event was sent.
     */
    private Instant sent;

    /**
     * The connection of this member
     */
    private Connection connection;

    /**
     * The revision of this event
     */
    private int revision;

    /**
     * The party ID.
     */
    @SerializedName("party_id")
    private String partyId;

    /**
     * The account ID of who joined
     */
    @SerializedName("account_id")
    private String accountId;

    /**
     * The display name of who joined
     */
    @SerializedName("account_dn")
    private String displayName;

    /**
     * The updated meta for this member.
     */
    @SerializedName("member_state_updated")
    private PartyMemberMeta updated;

    /**
     * When this member joined
     */
    @SerializedName("joined_at")
    private Instant joinedAt;

    /**
     * When this member was updated.
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * The local account
     */
    @Request(item = Account.class, local = true)
    private Account account;

    /**
     * Parties
     */
    @Request(item = Parties.class)
    private Parties parties;

    /**
     * The party service
     */
    @Request(item = PartyService.class)
    private PartyService partyService;

    /**
     * The friends service
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService friendsPublicService;

    /**
     * The accounts service
     */
    @Request(item = AccountPublicService.class)
    private AccountPublicService accountPublicService;

    /**
     * The party
     */
    private Party party;

    /**
     * @return When this event was sent.
     */
    public Instant sent() {
        return sent;
    }

    /**
     * @return The connection of this member
     */
    public Connection connection() {
        return connection;
    }

    /**
     * @return The revision of this event
     */
    public int revision() {
        return revision;
    }

    /**
     * @return The party ID.
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return The account ID of who joined
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return The display name of who joined
     */
    public String displayName() {
        return displayName;
    }

    /**
     * @return The updated meta for this member.
     */
    public PartyMemberMeta updated() {
        return updated;
    }

    /**
     * @return When this member joined
     */
    public Instant joinedAt() {
        return joinedAt;
    }

    /**
     * @return When this member was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return the party of this event
     */
    public Party party() {
        return party;
    }

    /**
     * Sets the party for this event
     *
     * @param party the party.
     */
    public void party(Party party) {
        this.party = party;
    }

    /**
     * Confirm this member to join.
     */
    public void confirm() {
        Requests.executeCall(partyService.confirm(partyId, accountId));
    }

    /**
     * Reject this member from joining.
     */
    public void reject() {
        Requests.executeCall(partyService.reject(partyId, accountId));
    }

    /**
     * @return the account of who joined.
     */
    public Account account() {
        return Requests.executeCall(accountPublicService.findByDisplayName(displayName));
    }

    /**
     * Retrieves the friend profile for this member.
     * They must be a friend for this to succeed.
     *
     * @return the friend profile
     */
    public Profile friendProfile() {
        return Requests.executeCall(friendsPublicService.profile(account.accountId(), accountId, true));
    }

}
