package athena.party.xmpp.event.member;

import athena.account.resource.Account;
import athena.context.AthenaContext;
import athena.friend.resource.summary.Profile;
import athena.party.resource.Party;
import athena.party.resource.connection.Connection;
import athena.party.resource.member.meta.PartyMemberMeta;
import athena.util.request.Requests;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;

/**
 * Represents an event for when a member disconnects from the party by closing their game.
 */
public final class PartyMemberDisconnectedEvent extends AthenaContext {

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
     * The account ID of who disconnected
     */
    @SerializedName("account_id")
    private String accountId;

    /**
     * The display name of who disconnected
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
     * @return The account ID of who disconnected
     */
    public String accountId() {
        return accountId;
    }

    /**
     * @return The display name of who disconnected
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
     * @return the account of who disconnected
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
        return Requests.executeCall(friendsPublicService.profile(localAccountId, accountId, true));
    }

}
