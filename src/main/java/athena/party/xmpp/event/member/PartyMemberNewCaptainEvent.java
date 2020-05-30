package athena.party.xmpp.event.member;

import athena.account.resource.Account;
import athena.account.service.AccountPublicService;
import athena.friend.resource.summary.Profile;
import athena.friend.service.FriendsPublicService;
import athena.party.resource.Party;
import athena.party.resource.member.PartyMember;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.util.json.request.Request;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents an event for when a member is promoted.
 */
public final class PartyMemberNewCaptainEvent {

    /**
     * When this event was sent.
     */
    private Instant sent;

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
     * The account ID of who was promoted
     */
    @SerializedName("account_id")
    private String accountId;

    /**
     * The display name of who was promoted
     */
    @SerializedName("account_dn")
    private String displayName;

    /**
     * The things updated
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
     * The accounts provider.
     */
    @Request(item = AccountPublicService.class)
    private AccountPublicService accountPublicService;

    /**
     * The friends service
     */
    @Request(item = FriendsPublicService.class)
    private FriendsPublicService friendsPublicService;

    /**
     * The party
     */
    private Party party;

    /**
     * The member
     */
    private PartyMember member;

    /**
     * @return When this event was sent.
     */
    public Instant sent() {
        return sent;
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
     * @return The account ID of who was promoted
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return The display name of who was promoted
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
     * @return the member related to this event
     */
    public PartyMember member() {
        return member;
    }

    /**
     * sets the member for this event
     *
     * @param member the member
     */
    public void member(PartyMember member) {
        this.member = member;
    }

    /**
     * @return the account of who was promoted
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

    /**
     * @return {@code true} if you were promoted.
     */
    public boolean localAccountPromoted() {
        return accountId.equals(account.accountId());
    }

}
