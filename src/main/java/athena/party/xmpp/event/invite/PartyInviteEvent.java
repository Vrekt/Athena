package athena.party.xmpp.event.invite;

import athena.context.AthenaContext;
import athena.party.resource.Party;
import athena.party.resource.meta.invites.PingOrInvitationMeta;
import com.google.gson.annotations.SerializedName;

import java.time.Instant;
import java.util.List;

/**
 * Represents a party invitation event.
 */
public final class PartyInviteEvent extends AthenaContext {

    /**
     * When this invite was sent.
     */
    @SerializedName("sent_at")
    private Instant sent;

    /**
     * The metadata of this invite
     */
    private PingOrInvitationMeta meta;

    /**
     * The ID of the party.
     */
    @SerializedName("party_id")
    private String partyId;

    /**
     * The account ID of who sent the invite
     */
    @SerializedName("inviter_id")
    private String inviterAccountId;

    /**
     * The display name of who sent the invite
     */
    @SerializedName("inviter_dn")
    private String inviterDisplayName;

    /**
     * The account ID of who is receiving the invite
     */
    @SerializedName("invitee_id")
    private String inviteeAccountId;

    /**
     * When this invite was updated
     */
    @SerializedName("updated_at")
    private Instant updatedAt;

    /**
     * The IDs of who is friends with who is receiving the invite.
     */
    @SerializedName("friends_ids")
    private List<String> friendIds;

    /**
     * The count of members in the party.
     */
    @SerializedName("members_count")
    private int membersCount;

    /**
     * The party
     */
    private Party party;

    /**
     * @return when the invite was sent
     */
    public Instant sent() {
        return sent;
    }

    /**
     * @return the metadata of this invite
     */
    public PingOrInvitationMeta meta() {
        return meta;
    }

    /**
     * @return the party ID.
     */
    public String partyId() {
        return partyId;
    }

    /**
     * @return The account ID of who sent the invite
     */
    public String inviterAccountId() {
        return inviterAccountId;
    }

    /**
     * @return The display name of who sent the invite
     */
    public String inviterDisplayName() {
        return inviterDisplayName;
    }

    /**
     * @return the account ID of who is receiving the invite
     */
    public String inviteeAccountId() {
        return inviteeAccountId;
    }

    /**
     * @return when this invite was updated.
     */
    public Instant updatedAt() {
        return updatedAt;
    }

    /**
     * @return The IDs of who is friends with who is receiving the invite.
     */
    public List<String> friendIds() {
        return friendIds;
    }

    /**
     * @return The count of members in the party.
     */
    public int membersCount() {
        return membersCount;
    }

    /**
     * @return the party of this ping.
     */
    public Party party() {
        return party;
    }

    /**
     * Sets the party for this ping.
     *
     * @param party the party.
     */
    public void party(Party party) {
        this.party = party;
    }

    /**
     * Joins the party that this invite belongs to.
     *
     * @return the party.
     */
    public Party joinParty() {
        if (party == null) throw new IllegalStateException("No party to join!");
        return parties.joinParty(party.partyId());
    }

}
